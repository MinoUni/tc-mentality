package io.teamchallenge.mentality.security.jwt;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.teamchallenge.mentality.customer.CustomerService;
import io.teamchallenge.mentality.exception.GithubTokenException;
import io.teamchallenge.mentality.exception.GoogleTokenException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class JwtHelper {

  private final GoogleIdTokenVerifier googleIdTokenVerifier;
  private final RestClient restClient;
  private final CustomerService userService;

  public JwtHelper(
      @Value("${application.google.client-id}") final String clientId,
      RestClient.Builder builder,
      CustomerService customerService) {
    this.googleIdTokenVerifier =
        new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
            .setAudience(Collections.singletonList(clientId))
            .build();
    this.restClient = builder.baseUrl("https://api.github.com").build();
    this.userService = customerService;
  }

  public String verifyToken(String token) {
    if (token.startsWith("gho_")) {
      Map<String, Object> payload = exchangeGithubToken(token);
      String email = (String) payload.get("email");
      if (!userService.existsByEmail(email)) {
        userService.create(payload);
      }
      return email;
    }
    try {
      GoogleIdToken idToken =
          Optional.ofNullable(googleIdTokenVerifier.verify(token))
              .orElseThrow(
                  () -> {
                    log.info("GoogleIdToken verification failed");
                    return new GoogleTokenException("GoogleIdToken verification failed");
                  });
      GoogleIdToken.Payload payload = idToken.getPayload();
      String email = payload.getEmail();
      if (!userService.existsByEmail(email)) {
        userService.create(payload);
      }
      return email;
    } catch (GeneralSecurityException e) {
      log.error("GoogleIdToken verification security failed:", e);
      throw new GoogleTokenException(e.getMessage());
    } catch (IOException e) {
      log.info("GoogleIdToken verification failed:", e);
      throw new GoogleTokenException(e.getMessage());
    }
  }

  private Map<String, Object> exchangeGithubToken(String tokenString) {
    return restClient
        .get()
        .uri("/user")
        .headers(httpHeaders -> httpHeaders.setBearerAuth(tokenString))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(
            HttpStatusCode::isError,
            (req, resp) -> {
              String message =
                  "Failed to retrieve github public profile information. StatusCode: [%d]"
                      .formatted(resp.getStatusCode().value());
              log.info(message);
              throw new GithubTokenException(message);
            })
        .body(new ParameterizedTypeReference<>() {});
  }
}
