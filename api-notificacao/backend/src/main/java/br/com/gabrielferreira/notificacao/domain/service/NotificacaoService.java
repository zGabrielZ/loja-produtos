package br.com.gabrielferreira.notificacao.domain.service;

import br.com.gabrielferreira.notificacao.domain.model.enums.NotificacaoStatusEnum;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;

public interface NotificacaoService {

    NotificacaoDTO enviarNotificacao(NotificacaoDTO notificacao);

    NotificacaoDTO salvarNotificacao(NotificacaoDTO notificacao, NotificacaoStatusEnum status);
}
