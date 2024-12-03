package io.teamchallenge.mentality.auth;

import io.teamchallenge.mentality.exception.GithubTokenException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubTokenValidator {

  private final RestClient restClient;

  public Map<String, Object> exchange(String tokenString) {
    return restClient
        .get()
        .uri("https://api.github.com/user")
        .headers(httpHeaders -> httpHeaders.setBearerAuth(tokenString.substring(7)))
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
