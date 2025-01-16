package io.teamchallenge.mentality.security.provider;

import io.teamchallenge.mentality.security.jwt.JwtHelper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final JwtHelper jwtHelper;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String email = jwtHelper.verifyToken(authentication.getName());
    return new UsernamePasswordAuthenticationToken(
        email, "", List.of(new SimpleGrantedAuthority("USER")));
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
