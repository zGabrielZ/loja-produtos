package br.com.gabrielferreira.produtos.domain.service;

import br.com.gabrielferreira.produtos.common.config.security.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsAutenticacaoService {

    UserDetailsImpl buscarUsuarioAutenticado();

    UserDetails loadUserByUsername(String email);

    UserDetailsImpl buscarUsuarioPorId(Long id);
}
