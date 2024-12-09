package io.teamchallenge.mentality.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final GoogleIdTokenValidator googleTokenValidator;
  private final GithubTokenValidator githubTokenValidator;
  private final AuthService authService;

  @PostMapping("google")
  public ResponseEntity<String> createFromGoogleToken(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String idTokenString) {
    GoogleIdToken idToken = googleTokenValidator.parse(idTokenString);
    if (!googleTokenValidator.validate(idToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Id token is invalid.");
    }
    Integer customerId = authService.createFromGoogleIdToken(idToken);
    return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customerId)
                .toUri())
        .build();
  }

  @PostMapping("github")
  public ResponseEntity<String> createFromGithubToken(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String tokenString) {
    Map<String, Object> payload = githubTokenValidator.exchange(tokenString);
    Integer customerId = authService.createFromGithubToken(payload);
    return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customerId)
                .toUri())
        .build();
  }
}
