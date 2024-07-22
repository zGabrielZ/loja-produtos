package br.com.gabrielferreira.notificacao.domain.service;

import br.com.gabrielferreira.notificacao.domain.model.Notificacao;
import br.com.gabrielferreira.notificacao.domain.model.enums.NotificacaoStatusEnum;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificacaoService {

    NotificacaoDTO enviarNotificacao(NotificacaoDTO notificacao);

    NotificacaoDTO salvarNotificacao(NotificacaoDTO notificacao, NotificacaoStatusEnum status);

    Page<Notificacao> buscarNotificacoes(String titulo, NotificacaoStatusEnum status, Pageable pageable);
}
