package io.teamchallenge.mentality.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    final String schemeName = "bearerAuth";
    return new OpenAPI()
        .info(new Info().title("TC-Mentality API").summary("API Documentation").version("1.0.0"))
        .addSecurityItem(new SecurityRequirement().addList(schemeName))
        .components(
            new Components()
                .addSecuritySchemes(
                    schemeName,
                    new SecurityScheme()
                        .name(schemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
        .servers(
            List.of(
                new Server().url("http://127.0.0.1:8080").description("local dev env"),
                new Server()
                    .url("https://tc-mentality.onrender.com")
                    .description("remote prod env")));
  }
}
