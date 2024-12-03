package io.teamchallenge.mentality.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

  public Integer createFromGoogleIdToken(GoogleIdToken idToken) {
    GoogleIdToken.Payload payload = idToken.getPayload();
    String email = payload.getEmail();
    log.debug("Extracted email from payload: {}", email);
    return 1;
  }

  public Integer createFromGithubToken(Map<String, Object> payload) {
    log.debug("Extracted public information from token: {}", payload);
    return 1;
  }
}
