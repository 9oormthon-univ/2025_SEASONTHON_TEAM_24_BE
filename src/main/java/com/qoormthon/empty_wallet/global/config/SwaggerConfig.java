package com.qoormthon.empty_wallet.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
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
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("bearerAuth",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .in(SecurityScheme.In.HEADER)
                    .name("Authorization"))
        )
        .info(new Info()
            .title("Empty-Wallet API")
            .description("API 호출에는 JWT 인증이 필요합니다.(role USER/ADMIN)")
            .version("1.0.0")
            .contact(
                new Contact()
                    .name("empty-wallet-backend-github")
                    .url("https://github.com")
            ))
        .servers(
            List.of(
                new Server().url("https://api.hackathoner.store").description("운영환경"),
                new Server().url("http://localhost:8080").description("개발환경")
            )
        )
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
  }
}
