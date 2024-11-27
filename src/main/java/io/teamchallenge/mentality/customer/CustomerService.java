package io.teamchallenge.mentality.customer;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerService {

  public Integer createFromIdToken(GoogleIdToken idToken) {
    Payload payload = idToken.getPayload();
    String email = payload.getEmail();
    log.debug("Extracted email from payload: {}", email);
    return 1;
  }
}
