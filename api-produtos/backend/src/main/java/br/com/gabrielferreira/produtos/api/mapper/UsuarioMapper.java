package br.com.gabrielferreira.produtos.api.mapper;

import br.com.gabrielferreira.produtos.api.dto.UsuarioDTO;
import br.com.gabrielferreira.produtos.api.dto.UsuarioResumidoDTO;
import br.com.gabrielferreira.produtos.api.dto.create.UsuarioCreateDTO;
import br.com.gabrielferreira.produtos.api.dto.update.UsuarioUpdateDTO;
import br.com.gabrielferreira.produtos.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = {FormatMapper.class, PerfilMapper.class})
public interface UsuarioMapper {

    Usuario toUsuario(UsuarioCreateDTO usuarioCreateDTO);

    Usuario toUsuario(UsuarioUpdateDTO usuarioUpdateDTO);

    @Mapping(target = "dataInclusao", qualifiedByName = "formatData")
    @Mapping(target = "dataAtualizacao", qualifiedByName = "formatData")
    UsuarioDTO toUsuarioDto(Usuario usuario);

    @Mapping(target = "dataInclusao", qualifiedByName = "formatData")
    @Mapping(target = "dataAtualizacao", qualifiedByName = "formatData")
    UsuarioResumidoDTO toUsuarioResumidoDto(Usuario usuario);

    default Page<UsuarioResumidoDTO> toUsuarioResumidoDtos(Page<Usuario> usuarios){
        return usuarios.map(this::toUsuarioResumidoDto);
    }
}
