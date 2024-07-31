package br.com.gabrielferreira.produtos.domain.service.impl;

import br.com.gabrielferreira.produtos.common.config.security.UserDetailsImpl;
import br.com.gabrielferreira.produtos.domain.exception.ForbiddenException;
import br.com.gabrielferreira.produtos.domain.exception.NaoEncontradoException;
import br.com.gabrielferreira.produtos.domain.exception.RegraDeNegocioException;
import br.com.gabrielferreira.produtos.domain.model.Perfil;
import br.com.gabrielferreira.produtos.domain.model.Usuario;
import br.com.gabrielferreira.produtos.domain.repository.UsuarioRepository;
import br.com.gabrielferreira.produtos.domain.service.PerfilService;
import br.com.gabrielferreira.produtos.domain.service.UserDetailsAutenticacaoService;
import br.com.gabrielferreira.produtos.domain.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static br.com.gabrielferreira.produtos.common.utils.ConstantesUtils.*;
import static br.com.gabrielferreira.produtos.domain.specification.UsuarioSpecification.*;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PerfilService perfilService;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsAutenticacaoService userDetailsAutenticacaoService;

    @Transactional
    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        validarCamposUsuario(usuario);
        validarSenhaUsuario(usuario.getSenha());
        validarEmail(usuario.getEmail(), null);

        UserDetailsImpl userDetailsAutenticado = userDetailsAutenticacaoService.buscarUsuarioAutenticado();
        validarCriacaoPerfisUsuarioAutenticado(usuario, userDetailsAutenticado);
        validarCriacaoPerfisUsuarioNaoAutenticado(usuario, userDetailsAutenticado);
        validarPerfis(usuario.getPerfis());

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
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

        UserDetailsImpl userDetailsAutenticado = userDetailsAutenticacaoService.buscarUsuarioAutenticado();
        validarAtualizacaoPerfisUsuarioAutenticado(usuario, userDetailsAutenticado);

        preencherCamposUsuario(usuarioEncontrado, usuario);

        if(userDetailsAutenticado.isAdmin()){
            validarPerfis(usuario.getPerfis());
            preencherCamposUsuarioPerfis(usuarioEncontrado, usuario);
        }

        usuarioEncontrado = usuarioRepository.save(usuarioEncontrado);
        return usuarioEncontrado;
    }

    @Transactional
    @Override
    public Usuario atualizarSenhaUsuario(Long id, String novaSenha, String antigaSenha) {
        Usuario usuarioEncontrado = buscarUsuarioPorId(id);

        validarSenhaUsuario(novaSenha);
        validarSenhaAntiga(antigaSenha, usuarioEncontrado.getSenha(), novaSenha, id);

        usuarioEncontrado.setSenha(passwordEncoder.encode(novaSenha));
        usuarioEncontrado = usuarioRepository.save(usuarioEncontrado);
        return usuarioEncontrado;
    }

    @Transactional
    @Override
    public void deletarUsuarioPorId(Long id) {
        UserDetailsImpl userDetailsAutenticado = userDetailsAutenticacaoService.buscarUsuarioAutenticado();
        if(userDetailsAutenticado.getId().equals(id)){
            throw new ForbiddenException("Você não pode excluir a sua própria conta no sistema");
        }

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

    @Override
    public void validarUsuarioAutenticado(Long idUsuario) {
        UserDetailsImpl userDetailsAutenticado = userDetailsAutenticacaoService.buscarUsuarioAutenticado();
        if(!userDetailsAutenticado.isAdmin() && !idUsuario.equals(userDetailsAutenticado.getId())){
            throw new ForbiddenException("Você não tem permissão para realizar a requisição");
        }
    }

    private void validarSenhaAntiga(String antigaSenha, String senhaCadastrada, String novaSenha, Long idUsuario){
        UserDetailsImpl userDetailsAutenticado = userDetailsAutenticacaoService.buscarUsuarioAutenticado();
        if(idUsuario.equals(userDetailsAutenticado.getId())){
            if(StringUtils.isBlank(antigaSenha)){
                throw new RegraDeNegocioException("É necessário informar a senha antiga");
            }

            if(!passwordEncoder.matches(antigaSenha, senhaCadastrada)){
                throw new RegraDeNegocioException("Senha antiga informada é incompatível");
            }

            if(passwordEncoder.matches(novaSenha, senhaCadastrada)){
                throw new RegraDeNegocioException("Nova senha é igual ao anterior");
            }
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
    }

    private void preencherCamposUsuarioPerfis(Usuario usuarioExistente, Usuario usuario){
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

    private void validarCriacaoPerfisUsuarioAutenticado(Usuario usuario, UserDetailsImpl userDetailsAutenticado){
        if(userDetailsAutenticado != null && userDetailsAutenticado.isAdmin() && usuario.getPerfis().isEmpty()){
            throw new RegraDeNegocioException("Não vai ser possível cadastrar este usuário pois o usuário autenticado não informou o perfil");
        } else if(userDetailsAutenticado != null && (userDetailsAutenticado.isFuncionario() || userDetailsAutenticado.isCliente())){
            throw new RegraDeNegocioException("Não vai ser possível cadastrar este usuário pois o usuário autenticado não é admin");
        }
    }

    private void validarCriacaoPerfisUsuarioNaoAutenticado(Usuario usuario, UserDetailsImpl userDetailsAutenticado){
        if(userDetailsAutenticado == null && !usuario.getPerfis().isEmpty()){
            throw new RegraDeNegocioException("Não vai ser possível cadastrar este usuário pois você não tem permissão para incluir perfil para este usuário");
        } else if(userDetailsAutenticado == null && usuario.getPerfis().isEmpty()){
            List<Perfil> perfis = new ArrayList<>();
            perfis.add(Perfil.builder().id(3L).build());
            usuario.setPerfis(perfis);
        }
    }

    private void validarAtualizacaoPerfisUsuarioAutenticado(Usuario usuario, UserDetailsImpl userDetailsAutenticado){
        if(userDetailsAutenticado.isAdmin() && usuario.getPerfis().isEmpty()){
            throw new RegraDeNegocioException("Não vai ser possível atualizar este usuário pois o usuário autenticado não informou o perfil");
        } else if(!userDetailsAutenticado.isAdmin() && !usuario.getPerfis().isEmpty()){
            throw new RegraDeNegocioException("Não vai ser possível atualizar este usuário pois o usuário autenticado não tem permissão de incluir ou alterar perfil");
        }
    }
}
