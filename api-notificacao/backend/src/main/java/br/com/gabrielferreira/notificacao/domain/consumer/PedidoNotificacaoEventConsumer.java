package br.com.gabrielferreira.notificacao.domain.consumer;

import br.com.gabrielferreira.notificacao.domain.publisher.PedidoNotificacaoEventDeadLetterQueuePublisher;
import br.com.gabrielferreira.notificacao.domain.service.NotificacaoService;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

import static br.com.gabrielferreira.notificacao.common.utils.DataUtils.*;

@Component
@Log4j2
@RequiredArgsConstructor
public class PedidoNotificacaoEventConsumer {

    private final NotificacaoService notificacaoService;

    private final PedidoNotificacaoEventDeadLetterQueuePublisher pedidoNotificacaoEventDeadLetterQueuePublisher;

    @RabbitListener(queues = "${broker.queue.pedido.notificacao.event}")
    public void listenPedidoNotificacaoEvent(@Payload NotificacaoDTO notificacaoDTO){
        log.debug("listenPedidoNotificacaoEvent notificação recebida : {}", notificacaoDTO);
        try {
            notificacaoDTO = notificacaoService.enviarNotificacao(notificacaoDTO);
            log.debug("Mensagem enviada com sucesso {}", notificacaoDTO);
            log.info("Mensagem enviada com sucesso");
        } catch (Exception e){
            log.error("listenPedidoNotificacaoEvent erro notificação : {}", e.getMessage());
            notificacaoDTO.setDataAtual(ZonedDateTime.now(UTC));
            notificacaoDTO.setDescricao(e.getMessage());
            pedidoNotificacaoEventDeadLetterQueuePublisher.publishPedidoNotificacaoEventDeadLetterQueue(notificacaoDTO);
        }
    }
}
