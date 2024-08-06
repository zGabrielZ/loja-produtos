package br.com.gabrielferreira.produtos.common.config.security;

import br.com.gabrielferreira.produtos.common.config.security.service.UserDetailsAutenticacaoService;
import br.com.gabrielferreira.produtos.domain.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class AppAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsAutenticacaoService usuarioAutenticacaoService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String senha = authentication.getCredentials().toString();

        UserDetails userDetails = usuarioAutenticacaoService.loadUserByUsername(email);
        boolean isMatchSenha = passwordEncoder.matches(senha, userDetails.getPassword());
        if(!isMatchSenha){
            log.warn("Senha do e-mail {} inválida", email);
            throw new RegraDeNegocioException("Senha inválida");
        }
        log.info("E-mail {} combina com a senha", email);
        UserDetailsImpl usuario = (UserDetailsImpl) userDetails;
        return new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
