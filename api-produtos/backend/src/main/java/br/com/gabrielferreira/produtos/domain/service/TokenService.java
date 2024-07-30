package br.com.gabrielferreira.produtos.domain.service;

import br.com.gabrielferreira.produtos.common.config.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;

public interface TokenService {

    String gerarToken(UserDetailsImpl userDetailsImpl);

    boolean isTokenValido(String token);

    Claims extrairTodoClaims(String token);
}
