package io.teamchallenge.mentality.customer;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import io.teamchallenge.mentality.auth.GoogleIdTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {

  private final GoogleIdTokenValidator tokenValidator;
  private final CustomerService customerService;

  @PostMapping
  public ResponseEntity<String> create(@RequestParam(name = "token") String idTokenString) {
    GoogleIdToken idToken = tokenValidator.parse(idTokenString);
    if (!tokenValidator.validate(idToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Id token is invalid.");
    }
    Integer customerId = customerService.createFromIdToken(idToken);
    return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customerId)
                .toUri())
        .build();
  }
}
