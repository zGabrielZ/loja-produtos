package br.com.gabrielferreira.notificacao.domain.consumer;

import br.com.gabrielferreira.notificacao.domain.publisher.PedidoNotificacaoEventDeadLetterQueuePublisher;
import br.com.gabrielferreira.notificacao.domain.publisher.PedidoNotificacaoEventParkingLotPublisher;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
@RequiredArgsConstructor
public class DeadLetterQueueListener {

    private static final String X_RETRY_HEADER = "x-dlq-retry";

    private final PedidoNotificacaoEventDeadLetterQueuePublisher pedidoNotificacaoEventDeadLetterQueuePublisher;

    private final PedidoNotificacaoEventParkingLotPublisher pedidoNotificacaoEventParkingLotPublisher;

    @RabbitListener(queues = "${broker.queue.pedido.notificacao.dlq}")
    public void listenerDeadLetterQueueListener(@Payload NotificacaoDTO notificacaoDTO, @Headers Map<String, Object> headers){
        log.debug("listenerDeadLetterQueueListener notificação recebida : {}", notificacaoDTO);

        Integer retryHeader = (Integer) headers.get(X_RETRY_HEADER);
        String codigoPedido = notificacaoDTO.getDados().get("codigoPedido").toString();

        if(retryHeader == null){
            retryHeader = 0;
        }

        log.info("Reprocessando notificação código pedido {}", codigoPedido);

        if(retryHeader < 3){
            Integer tryCount = retryHeader + 1;
            log.info("Reprocessando notificação código pedido {}. Tentativa {}", codigoPedido, tryCount);

            Map<String, Object> updateHeaders = new HashMap<>(headers);
            updateHeaders.put(X_RETRY_HEADER, tryCount);

            // Reprocessando
            MessagePostProcessor messagePostProcessor = message -> {
                MessageProperties messageProperties = message.getMessageProperties();
                updateHeaders.forEach(messageProperties::setHeader);
                return message;
            };

            log.info("Reenviando notificação código pedido {}", codigoPedido);
            pedidoNotificacaoEventDeadLetterQueuePublisher.publishPedidoNotificacaoEventDeadLetterQueue(notificacaoDTO, messagePostProcessor);
        } else {
            log.info("Reprocessamento falhou, enviando a notificação do código pedido {} para parking lot", codigoPedido);
            pedidoNotificacaoEventParkingLotPublisher.publishPedidoNotificacaoEventParkingLot(notificacaoDTO);
        }
    }
}
