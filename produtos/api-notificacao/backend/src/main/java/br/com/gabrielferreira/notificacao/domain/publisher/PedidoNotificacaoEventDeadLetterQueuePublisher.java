package br.com.gabrielferreira.notificacao.domain.publisher;

import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class PedidoNotificacaoEventDeadLetterQueuePublisher {

    private static final String MSG_LOG = "publishPedidoNotificacaoEventDeadLetterQueue enviado com sucesso";

    private final RabbitTemplate rabbitTemplate;

    private final String exchange;

    private final String routingKey;

    public PedidoNotificacaoEventDeadLetterQueuePublisher(RabbitTemplate rabbitTemplate,
                                                          @Value("${broker.exchange.pedido.notificacao.dlx}") String exchange,
                                                          @Value("${broker.key.pedido.notificacao.dlx}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publishPedidoNotificacaoEventDeadLetterQueue(NotificacaoDTO notificacaoDTO, MessagePostProcessor messagePostProcessor){
        log.debug("publishPedidoNotificacaoEventDeadLetterQueue mensagem : {}, exchange {}, rounting key {}, messagePostProcessor {}"
                , notificacaoDTO, exchange, routingKey, messagePostProcessor);

        rabbitTemplate.convertAndSend(exchange, routingKey, notificacaoDTO, messagePostProcessor);

        log.debug(MSG_LOG);
        log.info(MSG_LOG);
    }

    public void publishPedidoNotificacaoEventDeadLetterQueue(NotificacaoDTO notificacaoDTO){
        log.debug("publishPedidoNotificacaoEventDeadLetterQueue mensagem : {}, exchange {}, rounting key {}", notificacaoDTO, exchange, routingKey);

        rabbitTemplate.convertAndSend(exchange, routingKey, notificacaoDTO);

        log.debug(MSG_LOG);
        log.info(MSG_LOG);
    }
}
