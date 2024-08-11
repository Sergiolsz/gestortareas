package com.app.managertask.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  /**
   * Configura la información de la API para Swagger.
   *
   * @return OpenAPI
   */
  @Bean
  public OpenAPI swaggerAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Task Manager API")
            .description("API documentation for the Task Manager application")
            .version("v1.0.0")
            .contact(new Contact()
                .name("Support")
                .email("sergiolsz82@gmail.com"))
            .license(new License().name("OpenAPI documentation").url("http://springdoc.org")))
        .servers(List.of(
            new Server().url("http://localhost:8080").description("Local server")
        ));
  }

}