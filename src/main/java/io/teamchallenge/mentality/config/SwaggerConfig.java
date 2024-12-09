package io.teamchallenge.mentality.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info().title("TC-Mentality API").summary("API Documentation").version("1.0.0"))
        .servers(List.of(new Server().url("http://127.0.0.1:8080").description("local dev env")));
  }
}
