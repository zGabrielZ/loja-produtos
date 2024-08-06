package br.com.gabrielferreira.produtos.common.config.security.service;

import br.com.gabrielferreira.produtos.common.config.security.PerfilDetailsImpl;
import br.com.gabrielferreira.produtos.common.config.security.UserDetailsImpl;
import br.com.gabrielferreira.produtos.domain.exception.NaoEncontradoException;
import br.com.gabrielferreira.produtos.domain.exception.UnauthorizedException;
import br.com.gabrielferreira.produtos.domain.mapper.UserDetailsMapper;
import br.com.gabrielferreira.produtos.domain.model.Usuario;
import br.com.gabrielferreira.produtos.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserDetailsAutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private final UserDetailsMapper userDetailsMapper;

    public UserDetailsImpl buscarUsuarioAutenticado() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication != null && authentication.getPrincipal() != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetails){
                definirPerfilUsuario(userDetails);
                return userDetails;
            }
            return null;
        } catch (Exception e){
            log.error("Usuário inválido {}", e.getMessage());
            throw new UnauthorizedException("Usuário inválido");
        }
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarUsuarioComPerfisPorEmail(email);
        if(usuarioOpt.isEmpty()){
            log.warn("Usuário do e-mail {} não encontrado", email);
            throw new NaoEncontradoException(String.format("E-mail %s não encontrado", email));
        }
        log.info("Usuário do e-mail {} encontrado", email);
        return userDetailsMapper.toUserDetails(usuarioOpt.get());
    }

    public UserDetailsImpl buscarUsuarioPorId(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarUsuarioPorId(id);
        if(usuarioOpt.isEmpty()){
            log.warn("Usuário do id {} não encontrado", id);
            throw new NaoEncontradoException("Usuário não encontrado");
        }
        log.info("Usuário do id {} encontrado", id);
        return userDetailsMapper.toUserDetails(usuarioOpt.get());
    }

    private void definirPerfilUsuario(UserDetailsImpl userDetails){
        Map<String, Integer> perfilHierarquico = new HashMap<>();
        perfilHierarquico.put("ROLE_ADMIN", 3);
        perfilHierarquico.put("ROLE_FUNCIONARIO", 2);
        perfilHierarquico.put("ROLE_CLIENTE", 1);

        Integer maiorLevel = 0;
        for (PerfilDetailsImpl perfil : userDetails.getPerfis()) {
            Integer level = perfilHierarquico.get(perfil.getAutoriedade());
            if(level != null && level > maiorLevel){
                maiorLevel = level;
            }
        }

        if(maiorLevel.equals(3)){
            userDetails.setAdmin(true);
        } else if(maiorLevel.equals(2)){
            userDetails.setFuncionario(true);
        } else {
            userDetails.setCliente(true);
        }
    }
}
