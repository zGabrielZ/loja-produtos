package br.com.gabrielferreira.notificacao.domain.service.impl;

import br.com.gabrielferreira.notificacao.domain.mapper.NotificacaoMapper;
import br.com.gabrielferreira.notificacao.domain.mapper.UsuarioNotificacaoMapper;
import br.com.gabrielferreira.notificacao.domain.model.Notificacao;
import br.com.gabrielferreira.notificacao.domain.model.enums.NotificacaoStatusEnum;
import br.com.gabrielferreira.notificacao.domain.repository.NotificacaoRepository;
import br.com.gabrielferreira.notificacao.domain.service.EmailService;
import br.com.gabrielferreira.notificacao.domain.service.NotificacaoService;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificacaoServiceImpl implements NotificacaoService {

    private final EmailService emailService;

    private final NotificacaoRepository notificacaoRepository;

    private final NotificacaoMapper notificacaoMapper;

    private final UsuarioNotificacaoMapper usuarioNotificacaoMapper;

    @Transactional
    @Override
    public NotificacaoDTO enviarNotificacao(NotificacaoDTO notificacao) {
        notificacao = emailService.enviarEmail(notificacao);
        notificacao = salvarNotificacao(notificacao, NotificacaoStatusEnum.SUCESSO);
        return notificacao;
    }

    @Transactional
    @Override
    public NotificacaoDTO salvarNotificacao(NotificacaoDTO notificacao, NotificacaoStatusEnum status) {
        Notificacao notificacaoModel = notificacaoMapper.toNotificacao(notificacao);
        notificacaoModel.setStatus(status);
        notificacaoModel.setDestinatarios(usuarioNotificacaoMapper.toUsuariosNotificacoes(notificacao.getDestinatarios(), notificacaoModel));
        notificacaoRepository.save(notificacaoModel);
        return notificacao;
    }
}
