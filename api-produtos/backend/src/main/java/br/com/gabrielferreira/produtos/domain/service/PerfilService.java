package br.com.gabrielferreira.produtos.domain.service;

import br.com.gabrielferreira.produtos.domain.model.Perfil;

import java.util.List;

public interface PerfilService {

    Perfil buscarPerfilPorId(Long id);

    List<Perfil> buscarPerfis();
}
