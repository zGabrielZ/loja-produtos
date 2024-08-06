package br.com.gabrielferreira.notificacao.common.config.security;

import br.com.gabrielferreira.notificacao.api.exceptionhandler.ApiExceptionHandlerForbidden;
import br.com.gabrielferreira.notificacao.api.exceptionhandler.ApiExceptionHandlerUnauthorized;
import br.com.gabrielferreira.notificacao.api.mapper.ErroPadraoMapper;
import br.com.gabrielferreira.notificacao.common.config.security.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenService tokenService;

    private final ObjectMapper objectMapper;

    private final ErroPadraoMapper erroPadraoMapper;

    // Config senha criptografada
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Config de recurso estaticos
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/**.html"), new AntPathRequestMatcher("/**.css")
                , new AntPathRequestMatcher("/**.js"), new AntPathRequestMatcher("/v3/api-docs/**"), new AntPathRequestMatcher("/swagger-ui/**")
                , new AntPathRequestMatcher("/swagger-ui.html")));
    }

    // Config a partir da autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //Config seguranca
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Não é para criar sessão
                .csrf(AbstractHttpConfigurer::disable) // Desabilitar o csrf
                .addFilterBefore(new JWTValidatorTokenFilter(tokenService), UsernamePasswordAuthenticationFilter.class) // Verificar se o token está valido cada requisição
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .exceptionHandling(eh -> eh.authenticationEntryPoint(new ApiExceptionHandlerUnauthorized(objectMapper, erroPadraoMapper)) // Mensagem personalizada quando não for autenticado
                        .accessDeniedHandler(new ApiExceptionHandlerForbidden(objectMapper, erroPadraoMapper))) // Mensagem personalizada quando não tiver permissão
                .build();
    }

    // Config role hierarchy
    @Bean
    public RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_FUNCIONARIO \n ROLE_FUNCIONARIO > ROLE_CLIENTE";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }
}
