package br.com.gabrielferreira.produtos.domain.service.impl;

import br.com.gabrielferreira.produtos.domain.exception.NaoEncontradoException;
import br.com.gabrielferreira.produtos.domain.exception.RegraDeNegocioException;
import br.com.gabrielferreira.produtos.domain.model.Perfil;
import br.com.gabrielferreira.produtos.domain.model.Usuario;
import br.com.gabrielferreira.produtos.domain.repository.UsuarioRepository;
import br.com.gabrielferreira.produtos.domain.service.PerfilService;
import br.com.gabrielferreira.produtos.domain.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static br.com.gabrielferreira.produtos.common.utils.ConstantesUtils.*;
import static br.com.gabrielferreira.produtos.domain.specification.UsuarioSpecification.*;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PerfilService perfilService;

    @Transactional
    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        validarCamposUsuario(usuario);
        validarSenhaUsuario(usuario.getSenha());
        validarEmail(usuario.getEmail(), null);
        validarPerfis(usuario.getPerfis());

        usuario = usuarioRepository.save(usuario);
        return usuario;
    }

    @Override
    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.buscarUsuarioPorId(id)
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado", id));
    }

    @Transactional
    @Override
    public Usuario atualizarUsuario(Long id, Usuario usuario) {
        Usuario usuarioEncontrado = buscarUsuarioPorId(id);
        validarPerfis(usuario.getPerfis());

        preencherCamposUsuario(usuarioEncontrado, usuario);

        usuarioEncontrado = usuarioRepository.save(usuarioEncontrado);
        return usuarioEncontrado;
    }

    @Transactional
    @Override
    public Usuario atualizarSenhaUsuario(Long id, String novaSenha, String antigaSenha) {
        Usuario usuarioEncontrado = buscarUsuarioPorId(id);

        validarSenhaUsuario(novaSenha);
        validarSenhaAntiga(antigaSenha, usuarioEncontrado.getSenha(), novaSenha);
        usuarioEncontrado.setSenha(novaSenha);

        usuarioEncontrado = usuarioRepository.save(usuarioEncontrado);
        return usuarioEncontrado;
    }

    @Transactional
    @Override
    public void deletarUsuarioPorId(Long id) {
        Usuario usuarioEncontrado = buscarUsuarioPorId(id);
        usuarioRepository.delete(usuarioEncontrado);
    }

    @Override
    public Page<Usuario> buscarUsuariosPaginados(Pageable pageable, String nome, String email) {
        Specification<Usuario> specification = Specification.where(
                buscarPorNome(nome)
                        .and(buscarPorEmail(email))
        );

        return usuarioRepository.findAll(specification, pageable);
    }

    @Override
    public boolean existeUsuarioPorId(Long id) {
        return usuarioRepository.existsUsuarioPorId(id);
    }

    @Override
    public boolean naoExisteUsuarioPorId(Long id) {
        return !existeUsuarioPorId(id);
    }

    @Override
    public boolean existeUsuarioComPedido(Long id, Long idPedido) {
        return usuarioRepository.existsUsuarioComPedidoPorId(id, idPedido);
    }

    @Override
    public boolean naoExisteUsuarioComPedido(Long id, Long idPedido) {
        return !existeUsuarioComPedido(id, idPedido);
    }

    private void validarSenhaAntiga(String antigaSenha, String senhaCadastrada, String novaSenha){
        if(!antigaSenha.equals(senhaCadastrada)){
            throw new RegraDeNegocioException("Senha antiga informada é incompatível");
        }

        if(novaSenha.equals(senhaCadastrada)){
            throw new RegraDeNegocioException("Nova senha é igual ao anterior");
        }
    }

    private void validarSenhaUsuario(String senha){
        if(!isPossuiCaracteresEspecias(senha)){
            throw new RegraDeNegocioException("A senha informada tem que ter pelo menos uma caractere especial");
        }

        if(!isPossuiCaractereMaiusculas(senha)){
            throw new RegraDeNegocioException("A senha informada tem que ter pelo menos uma caractere maiúsculas");
        }

        if(!isPossuiCaractereMinusculas(senha)){
            throw new RegraDeNegocioException("A senha informada tem que ter pelo menos uma caractere minúsculas");
        }

        if(!isPossuiCaractereDigito(senha)){
            throw new RegraDeNegocioException("A senha informada tem que ter pelo menos um caractere dígito");
        }
    }

    private void validarCamposUsuario(Usuario usuario){
        usuario.setNome(usuario.getNome().trim());
        usuario.setEmail(usuario.getEmail().trim());
    }

    private void validarEmail(String email, Long id){
        if(isEmailExistente(email, id)){
            throw new RegraDeNegocioException(String.format("Não vai ser possível cadastrar este usuário pois o e-mail '%s' já foi cadastrado", email));
        }
    }

    private boolean isEmailExistente(String email, Long id){
        if(id == null){
            return usuarioRepository.buscarPorEmail(email)
                    .isPresent();
        } else {
            return usuarioRepository.buscarPorEmail(email)
                    .filter(c -> !c.getId().equals(id))
                    .isPresent();
        }
    }

    private void validarPerfis(List<Perfil> perfis){
        validarPerfilExistente(perfis);
        validarPefilDuplicados(perfis);
    }

    private void validarPerfilExistente(List<Perfil> perfis){
        perfis.forEach(perfil -> {
            Perfil perfilEncontrado = perfilService.buscarPerfilPorId(perfil.getId());
            perfil.setDescricao(perfilEncontrado.getDescricao());
            perfil.setAutoriedade(perfilEncontrado.getAutoriedade());
        });
    }

    private void validarPefilDuplicados(List<Perfil> perfis){
        List<Long> idsPerfis = perfis.stream().map(Perfil::getId).toList();
        idsPerfis.forEach(idPerfil -> {
            int duplicados = Collections.frequency(idsPerfis, idPerfil);

            if(duplicados > 1){
                throw new RegraDeNegocioException("Não vai ser possível cadastrar este usuário pois tem perfis duplicados ou mais de duplicados");
            }
        });
    }

    private void preencherCamposUsuario(Usuario usuarioExistente, Usuario usuario){
        usuarioExistente.setNome(usuario.getNome().trim());

        removerPerfisNaoExistentes(usuarioExistente.getPerfis(), usuario.getPerfis());
        incluirNovosPerfis(usuarioExistente.getPerfis(), usuario.getPerfis());
    }

    private void removerPerfisNaoExistentes(List<Perfil> perfisExistentes, List<Perfil> novosPerfis){
        perfisExistentes.removeIf(perfisExistente -> perfisExistente.isNaoContemPerfil(novosPerfis));
    }

    private void incluirNovosPerfis(List<Perfil> perfisExistentes, List<Perfil> novosPerfis){
        novosPerfis.forEach(novoPerfil -> {
            if(novoPerfil.isNaoContemPerfil(perfisExistentes)){
                perfisExistentes.add(novoPerfil);
            }
        });
    }
}
