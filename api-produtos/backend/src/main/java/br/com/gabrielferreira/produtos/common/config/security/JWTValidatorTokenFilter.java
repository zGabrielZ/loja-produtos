package br.com.gabrielferreira.produtos.common.config.security;

import br.com.gabrielferreira.produtos.domain.service.TokenService;
import br.com.gabrielferreira.produtos.domain.service.UserDetailsAutenticacaoService;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Log4j2
public class JWTValidatorTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UserDetailsAutenticacaoService userDetailsAutenticacaoService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Recuperar token que foi enviado
        String token = recuperarToken(request);

        // Verificar se o token está valido, caso estiver valido, autentica o usuário
        if(token != null && tokenService.isTokenValido(token)){
            try {
                Claims claims = tokenService.extrairTodoClaims(token);
                // Caso exstir o claims, pegar o id do usuário e autenticar
                if(claims != null){
                    autenticarUsuario(claims);
                }
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

    private void autenticarUsuario(Claims claims){
        Long idUsuario = Long.valueOf(String.valueOf(claims.get("idUsuario")));
        UserDetailsImpl userDetails = userDetailsAutenticacaoService.buscarUsuarioPorId(idUsuario);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getPerfis());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
