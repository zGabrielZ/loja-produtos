package br.com.gabrielferreira.produtos.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI(){
        Info info = new Info()
                .title("API Produto")
                .version("1.0")
                .contact(contact())
                .description("API de Produtos")
                .license(license());

        return new OpenAPI()
                .info(info);
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
        license.setName("Licença API Evento");
        return license;
    }

}
