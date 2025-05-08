package com.marcelohofart.bank_api.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SwaggerConfig {
    @Autowired
    private Environment environment;

    @Bean
    public OpenAPI customAPI(){
        return new OpenAPI().info(
                new Info()
                        .title("Bank API")
                        .description("Api para realizar lançamentos e transações entre contas"));
    }
}
