package br.com.gabrielferreira.notificacao.domain.service;

import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;

public interface EmailService {

    NotificacaoDTO enviarEmail(NotificacaoDTO notificacao);
}
