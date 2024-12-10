package io.teamchallenge.mentality.auth;

import io.teamchallenge.mentality.exception.GithubTokenException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class GithubTokenClient {

  private final RestClient restClient;

  public GithubTokenClient(RestClient.Builder builder) {
    this.restClient = builder.baseUrl("https://api.github.com").build();
  }

  public Map<String, Object> exchange(String authorization) {
    final String tokenString = authorization.substring(7);
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
