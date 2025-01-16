package io.teamchallenge.mentality.security.filter;

import io.teamchallenge.mentality.customer.CustomerService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAccessTokenAuthenticationFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;
  private final CustomerService userService;

  public JwtAccessTokenAuthenticationFilter(
      AuthenticationManager authenticationManager, CustomerService userService) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String token =
        Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
            .filter(header -> header.startsWith("Bearer "))
            .map(jwt -> jwt.substring(7))
            .orElse(null);
    if (token != null) {
      try {
        Authentication authentication =
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(token, null));
        if (userService.existsByEmail((String) authentication.getPrincipal())) {
          log.debug("User authentication was successful: {}", authentication.getPrincipal());
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (Exception e) {
        log.info("Access was denied for req with token: {}", e.getMessage());
      }
    }
    filterChain.doFilter(request, response);
  }
}
