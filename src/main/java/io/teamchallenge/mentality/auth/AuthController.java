package io.teamchallenge.mentality.auth;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.teamchallenge.mentality.exception.InvalidGoogleTokenException;
import io.teamchallenge.mentality.exception.dto.ApiErrorResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Tag(
    name = "Authentication endpoints",
    description = "Endpoints for extracting customer public information from the token payload")
class AuthController {

  private final GoogleIdTokenValidator googleTokenValidator;
  private final GithubTokenClient githubTokenClient;
  private final AuthService authService;

  @Operation(
      summary = "Create customer from extracted google id-token payload",
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "New customer created",
            headers = {
              @Header(
                  name = "Location",
                  description = "URI to resource",
                  schema = @Schema(type = "string"))
            },
            content = {@Content}),
        @ApiResponse(
            responseCode = "401",
            description = "Id token is invalid",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            })
      })
  @PostMapping("google")
  public ResponseEntity<String> createFromGoogleToken(
      @RequestHeader(AUTHORIZATION) String idTokenString) {
    GoogleIdToken idToken = googleTokenValidator.parse(idTokenString);
    if (!googleTokenValidator.validate(idToken)) {
      throw new InvalidGoogleTokenException();
    }
    Integer customerId = authService.createFromGoogleIdToken(idToken);
    return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customerId)
                .toUri())
        .build();
  }

  @Operation(
      summary = "Create customer from extracted github token payload",
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "New customer created",
            headers = {
              @Header(
                  name = "Location",
                  description = "URI to resource",
                  schema = @Schema(type = "string"))
            },
            content = {@Content}),
        @ApiResponse(
            responseCode = "401",
            description = "Token is invalid",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            })
      })
  @PostMapping("github")
  public ResponseEntity<String> createFromGithubToken(
      @RequestHeader(AUTHORIZATION) String tokenString) {
    Map<String, Object> payload = githubTokenClient.exchange(tokenString);
    Integer customerId = authService.createFromGithubToken(payload);
    return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customerId)
                .toUri())
        .build();
  }
}
