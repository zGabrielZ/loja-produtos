package br.com.gabrielferreira.notificacao.domain.mapper;

import br.com.gabrielferreira.notificacao.domain.model.Notificacao;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotificacaoMapper {

    NotificacaoMapper INSTANCE = Mappers.getMapper(NotificacaoMapper.class);

    @Mapping(target = "destinatarios", ignore = true)
    @Mapping(target = "dataAtualErro", source = "dataAtual")
    @Mapping(target = "descricaoErro", source = "descricao")
    Notificacao toNotificacao(NotificacaoDTO notificacaoDTO);
}
