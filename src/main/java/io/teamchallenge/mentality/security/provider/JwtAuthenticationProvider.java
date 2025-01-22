package io.teamchallenge.mentality.security.provider;

import io.teamchallenge.mentality.security.jwt.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final JwtHelper jwtHelper;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    User user = jwtHelper.verifyToken(authentication.getName());
    return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
