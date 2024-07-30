package br.com.gabrielferreira.produtos.api.mapper;

import br.com.gabrielferreira.produtos.api.dto.TokenDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TokenMapper {

    @Mapping(source = "tipo", target = "tipo")
    @Mapping(source = "token", target = "token")
    TokenDTO toTokenDTO(String token, String tipo);
}
