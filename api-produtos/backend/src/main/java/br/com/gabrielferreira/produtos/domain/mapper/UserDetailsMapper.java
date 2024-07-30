package br.com.gabrielferreira.produtos.domain.mapper;

import br.com.gabrielferreira.produtos.common.config.security.UserDetailsImpl;
import br.com.gabrielferreira.produtos.domain.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDetailsMapper {

    UserDetailsImpl toUserDetails(Usuario usuario);
}
