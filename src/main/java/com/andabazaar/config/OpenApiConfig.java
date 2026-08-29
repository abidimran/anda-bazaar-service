package com.andabazaar.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Anda Bazaar API")
                        .description("Egg Market Price Rate Application")
                        .version("1.0.0")
                        .contact(new Contact() .name("Anda Bazaar") .email("support@andabazaar.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .schemaRequirement("Bearer Token", new SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Enter your JWT token"));
    }
}
