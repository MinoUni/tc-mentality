package io.teamchallenge.mentality.config;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import io.teamchallenge.mentality.customer.CustomerService;
import io.teamchallenge.mentality.security.filter.JwtAccessTokenAuthenticationFilter;
import io.teamchallenge.mentality.security.provider.JwtAuthenticationProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@RequiredArgsConstructor
public class SecurityConfig {

  private static final String CUSTOMER_URL = "/customers/{id}";

  private final AuthenticationConfiguration authenticationConfiguration;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  @Value("${application.cors.allowed-origins}")
  private String[] allowedOrigins;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowedOrigins(List.of(allowedOrigins));
    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    corsConfig.setAllowedHeaders(
        List.of(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Headers",
            "X-Requested-With",
            "Origin",
            "Content-Type",
            "Accept",
            "Authorization"));
    corsConfig.setAllowCredentials(true);
    corsConfig.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();
    corsConfigSource.registerCorsConfiguration("/**", corsConfig);
    return corsConfigSource;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomerService customerService)
      throws Exception {
    return http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .addFilterBefore(
            new JwtAccessTokenAuthenticationFilter(authenticationManager(), customerService),
            UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(
            exception ->
                exception
                    .authenticationEntryPoint(
                        (req, resp, e) -> resp.sendError(SC_UNAUTHORIZED, "Authorization required"))
                    .accessDeniedHandler(
                        (req, resp, e) -> resp.sendError(SC_FORBIDDEN, "Insufficient authorities")))
        .authorizeHttpRequests(
            req ->
                req.requestMatchers(
                        "/v2/api-docs/**",
                        "/v3/api-docs/**",
                        "/swagger.json",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/webjars/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/products/**", "/customers/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/products/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.PUT, CUSTOMER_URL)
                    .authenticated()
                    .requestMatchers(HttpMethod.PATCH, CUSTOMER_URL)
                    .authenticated()
                    .requestMatchers(HttpMethod.DELETE, CUSTOMER_URL)
                    .authenticated()
                    .anyRequest()
                    .authenticated())
        .build();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(jwtAuthenticationProvider);
  }
}
