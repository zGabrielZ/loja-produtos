package br.com.gabrielferreira.produtos.api.mapper;

import br.com.gabrielferreira.produtos.api.dto.PerfilDTO;
import br.com.gabrielferreira.produtos.api.dto.create.PerfilCreateDTO;
import br.com.gabrielferreira.produtos.domain.model.Perfil;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PerfilMapper {

    PerfilDTO toPerfilDto(Perfil perfil);

    default List<PerfilDTO> toPerfisDtos(List<Perfil> perfis){
        return perfis.stream().map(this::toPerfilDto).toList();
    }

    Perfil toPerfil(PerfilCreateDTO perfilCreateDTO);

    default List<Perfil> toPerfis(List<PerfilCreateDTO> perfilCreateDTOS){
        return perfilCreateDTOS.stream().map(this::toPerfil).toList();
    }
}
