package br.com.gabrielferreira.notificacao.api.mapper;

import br.com.gabrielferreira.notificacao.api.dto.NotificacaoResumidoDTO;
import br.com.gabrielferreira.notificacao.domain.model.Notificacao;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface NotificacaoDTOMapper {

    NotificacaoResumidoDTO toNotificacaoDto(Notificacao notificacao);

    default Page<NotificacaoResumidoDTO> toNotificacoesDtos(Page<Notificacao> notificacaos){
        return notificacaos.map(this::toNotificacaoDto);
    }
}
