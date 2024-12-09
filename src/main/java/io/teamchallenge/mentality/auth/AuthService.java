package io.teamchallenge.mentality.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import io.teamchallenge.mentality.customer.Customer;
import io.teamchallenge.mentality.customer.CustomerRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final CustomerRepository customerRepository;

  public Integer createFromGoogleIdToken(GoogleIdToken idToken) {
    GoogleIdToken.Payload payload = idToken.getPayload();
    String email = payload.getEmail();
    if (customerRepository.existsByEmail(email)) {
      return customerRepository.getCustomerIdByEmail(email);
    }
    Customer customer =
        Customer.builder()
            .email(email)
            .firstName((String) payload.get("given_name"))
            .lastName((String) payload.get("family_name"))
            .build();
    customer = customerRepository.save(customer);
    return customer.getId();
  }

  public Integer createFromGithubToken(Map<String, Object> payload) {
    String email = (String) payload.get("email");
    if (customerRepository.existsByEmail(email)) {
      return customerRepository.getCustomerIdByEmail(email);
    }
    String[] fullName = ((String) payload.get("name")).split("\\s+");
    Customer customer =
        Customer.builder()
            .email(email)
            .firstName(fullName[0])
            .lastName(fullName[1])
            .build();
    customer = customerRepository.save(customer);
    return customer.getId();
  }
}
