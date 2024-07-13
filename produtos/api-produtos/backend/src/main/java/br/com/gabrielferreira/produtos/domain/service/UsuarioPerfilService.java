package br.com.gabrielferreira.produtos.domain.service;

import br.com.gabrielferreira.produtos.domain.model.Perfil;

import java.util.List;

public interface UsuarioPerfilService {

    Perfil buscarPerfilPorId(Long idUsuario, Long id);

    List<Perfil> buscarPerfisPorIdUsuario(Long idUsuario);
}
