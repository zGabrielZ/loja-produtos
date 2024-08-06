package br.com.gabrielferreira.notificacao.common.config.security.service;

import br.com.gabrielferreira.notificacao.common.config.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsAutenticacaoService {

    public UserDetailsImpl buscarUsuarioAutenticado(){
        return (UserDetailsImpl) getAuthentication().getPrincipal();
    }

    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
