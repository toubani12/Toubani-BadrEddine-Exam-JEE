package toubani.badreddine.carloacation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carRentalOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Car Rental API")
                        .description("REST API for managing agencies, vehicles (cars & motorcycles) and rentals.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Toubani Badreddine")
                                .email("fadre6@gmail.com"))
                        .license(new License().name("Apache 2.0")));
    }
}
