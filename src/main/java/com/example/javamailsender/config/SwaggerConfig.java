package com.example.javamailsender.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI Configuration
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OTP Mail Sender API")
                        .version("1.0.0")
                        .description("REST API for sending OTP verification emails via Gmail")
                        .contact(new Contact()
                                .name("Support")
                                .url("https://github.com")));
    }
}
