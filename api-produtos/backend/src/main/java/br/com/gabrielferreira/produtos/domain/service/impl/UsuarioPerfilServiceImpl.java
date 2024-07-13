package br.com.gabrielferreira.produtos.domain.service.impl;

import br.com.gabrielferreira.produtos.domain.exception.NaoEncontradoException;
import br.com.gabrielferreira.produtos.domain.model.Perfil;
import br.com.gabrielferreira.produtos.domain.repository.PerfilRepository;
import br.com.gabrielferreira.produtos.domain.service.UsuarioPerfilService;
import br.com.gabrielferreira.produtos.domain.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioPerfilServiceImpl implements UsuarioPerfilService {

    private final PerfilRepository perfilRepository;

    private final UsuarioService usuarioService;

    @Override
    public Perfil buscarPerfilPorId(Long idUsuario, Long id) {
        validarUsuarioExistente(idUsuario);
        return perfilRepository.buscarPerfilPorId(idUsuario, id)
                .orElseThrow(() -> new NaoEncontradoException("Perfil não encontrado", idUsuario, id));
    }

    @Override
    public List<Perfil> buscarPerfisPorIdUsuario(Long idUsuario) {
        validarUsuarioExistente(idUsuario);
        return perfilRepository.buscarPerfisPorIdUsuario(idUsuario);
    }

    private void validarUsuarioExistente(Long idUsuario){
        if(usuarioService.naoExisteUsuarioPorId(idUsuario)){
            throw new NaoEncontradoException("Usuário não encontrado", idUsuario);
        }
    }
}
