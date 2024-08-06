package br.com.gabrielferreira.notificacao.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI(){
        Info info = new Info()
                .title("API Notificação")
                .version("1.0")
                .contact(contact())
                .description("API de Notificações")
                .license(license());

        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", securityScheme()));
    }

    private Contact contact(){
        Contact contact = new Contact();
        contact.setName("Gabriel Ferreira");
        contact.setEmail("ferreiragabriel2612@gmail.com");
        contact.setUrl("https://github.com/zGabrielZ");
        return contact;
    }

    private License license(){
        License license = new License();
        license.setName("Licença API Notificações");
        return license;
    }

    private SecurityScheme securityScheme(){
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

}
