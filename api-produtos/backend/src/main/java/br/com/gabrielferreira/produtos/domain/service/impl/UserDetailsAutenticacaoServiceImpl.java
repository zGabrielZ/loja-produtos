package br.com.gabrielferreira.produtos.domain.service.impl;

import br.com.gabrielferreira.produtos.common.config.security.UserDetailsImpl;
import br.com.gabrielferreira.produtos.domain.exception.NaoEncontradoException;
import br.com.gabrielferreira.produtos.domain.exception.UnauthorizedException;
import br.com.gabrielferreira.produtos.domain.mapper.UserDetailsMapper;
import br.com.gabrielferreira.produtos.domain.model.Usuario;
import br.com.gabrielferreira.produtos.domain.repository.UsuarioRepository;
import br.com.gabrielferreira.produtos.domain.service.UserDetailsAutenticacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserDetailsAutenticacaoServiceImpl implements UserDetailsAutenticacaoService, UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private final UserDetailsMapper userDetailsMapper;

    @Override
    public UserDetailsImpl buscarUsuarioAutenticado() {
        try {
            return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e){
            log.error("Usuário inválido {}", e.getMessage());
            throw new UnauthorizedException("Usuário inválido");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarUsuarioComPerfisPorEmail(email);
        if(usuarioOpt.isEmpty()){
            log.warn("Usuário do e-mail {} não encontrado", email);
            throw new NaoEncontradoException(String.format("E-mail %s não encontrado", email));
        }
        log.info("Usuário do e-mail {} encontrado", email);
        return userDetailsMapper.toUserDetails(usuarioOpt.get());
    }

    @Override
    public UserDetailsImpl buscarUsuarioPorId(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarUsuarioPorId(id);
        if(usuarioOpt.isEmpty()){
            log.warn("Usuário do id {} não encontrado", id);
            throw new NaoEncontradoException("Usuário não encontrado");
        }
        log.info("Usuário do id {} encontrado", id);
        return userDetailsMapper.toUserDetails(usuarioOpt.get());
    }
}
