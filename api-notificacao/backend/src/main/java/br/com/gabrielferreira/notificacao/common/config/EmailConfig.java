package br.com.gabrielferreira.notificacao.common.config;

import br.com.gabrielferreira.notificacao.domain.service.impl.GoogleEmailServiceImpl;
import br.com.gabrielferreira.notificacao.domain.service.impl.LogEmailServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

@Configuration
public class EmailConfig {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    private final String emailRemetente;

    public EmailConfig(JavaMailSender javaMailSender, TemplateEngine templateEngine, @Value("${spring.mail.username}") String emailRemetente) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.emailRemetente = emailRemetente;
    }

    @Primary
    @Bean
    public GoogleEmailServiceImpl googleEmailService(){
        return new GoogleEmailServiceImpl(javaMailSender, templateEngine, emailRemetente);
    }

    @Bean
    public LogEmailServiceImpl logEmailService(){
        return new LogEmailServiceImpl();
    }
}
