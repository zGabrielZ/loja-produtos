package br.com.gabrielferreira.notificacao.domain.service.impl;

import br.com.gabrielferreira.notificacao.domain.service.EmailService;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LogEmailServiceImpl implements EmailService {

    @Override
    public NotificacaoDTO enviarEmail(NotificacaoDTO notificacao) {
        log.debug("enviarEmail notificação : {}", notificacao);
        return notificacao;
    }
}
