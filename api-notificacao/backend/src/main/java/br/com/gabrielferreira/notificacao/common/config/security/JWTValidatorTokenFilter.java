package br.com.gabrielferreira.notificacao.common.config.security;

import br.com.gabrielferreira.notificacao.common.config.security.service.TokenService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Log4j2
public class JWTValidatorTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Recuperar token que foi enviado
        String token = recuperarToken(request);

        // Verificar se o token está valido, caso estiver valido, autentica o usuário
        if(token != null && tokenService.isTokenValido(token)){
            try {
                String email = tokenService.getEmail(token);
                String perfis = tokenService.getPerfis(token);
                UserDetailsImpl userDetails = UserDetailsImpl.construirUserDetails(email, perfis);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception e){
                log.error("Erro ao autenticar usuário: {}", e.getMessage());
            }
        }


        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request){
        String headerToken = request.getHeader("Authorization");
        if(StringUtils.isBlank(headerToken) || !headerToken.startsWith("Bearer ")){
            return null;
        }
        return headerToken.substring(7);
    }
}
