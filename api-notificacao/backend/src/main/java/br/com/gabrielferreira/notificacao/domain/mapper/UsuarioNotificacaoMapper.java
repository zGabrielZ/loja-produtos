package br.com.gabrielferreira.notificacao.domain.mapper;

import br.com.gabrielferreira.notificacao.domain.model.Notificacao;
import br.com.gabrielferreira.notificacao.domain.model.UsuarioNotificacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioNotificacaoMapper {

    UsuarioNotificacaoMapper INSTANCE = Mappers.getMapper(UsuarioNotificacaoMapper.class);

    @Mapping(target = "email", source = "destinatario")
    @Mapping(target = "notificacao", source = "notificacao")
    UsuarioNotificacao toUsuarioNotificacao(String destinatario, Notificacao notificacao);

    default List<UsuarioNotificacao> toUsuariosNotificacoes(String[] destinatarios, Notificacao notificacao){
        return Arrays.stream(destinatarios).map(d -> toUsuarioNotificacao(d, notificacao))
                .toList();
    }
}
