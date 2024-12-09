package io.teamchallenge.mentality.auth;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.teamchallenge.mentality.exception.GoogleTokenException;
import java.io.IOException;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GoogleIdTokenValidator {

  @Value("${application.google.client-id}")
  private String clientId;

  private final GoogleIdTokenVerifier verifier;

  public GoogleIdTokenValidator() {
    this.verifier =
        new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
            .setAudience(Collections.singletonList(clientId))
            .build();
  }

  public GoogleIdToken parse(String idTokenString) {
    try {
      return GoogleIdToken.parse(verifier.getJsonFactory(), idTokenString);
    } catch (IOException e) {
      log.info("Failed to parse Id token string. ", e);
      throw new GoogleTokenException(e.getMessage());
    }
  }

  public boolean validate(IdToken idToken) {
    try {
      return verifier.verifyOrThrow(idToken);
    } catch (IOException e) {
      log.info("Id token verification fails to run. ", e);
      throw new GoogleTokenException(e.getMessage());
    }
  }
}
