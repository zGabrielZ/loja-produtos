package br.com.gabrielferreira.produtos.domain.service;

import br.com.gabrielferreira.produtos.domain.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    Usuario salvarUsuario(Usuario usuario);

    Usuario buscarUsuarioPorId(Long id);

    Usuario atualizarUsuario(Long id, Usuario usuario);

    Usuario atualizarSenhaUsuario(Long id, String novaSenha, String antigaSenha);

    void deletarUsuarioPorId(Long id);

    Page<Usuario> buscarUsuariosPaginados(Pageable pageable, String nome, String email);

    boolean existeUsuarioPorId(Long id);

    boolean naoExisteUsuarioPorId(Long id);

    boolean existeUsuarioComPedido(Long id, Long idPedido);

    boolean naoExisteUsuarioComPedido(Long id, Long idPedido);
}
