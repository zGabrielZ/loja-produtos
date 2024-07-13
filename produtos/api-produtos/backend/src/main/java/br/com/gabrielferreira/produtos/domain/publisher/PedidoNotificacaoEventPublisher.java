package br.com.gabrielferreira.produtos.domain.publisher;

import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class PedidoNotificacaoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    private final String exchange;

    private final String routingKey;

    public PedidoNotificacaoEventPublisher(RabbitTemplate rabbitTemplate, @Value("${broker.exchange.pedido.notificacao.event}") String exchange,
                                           @Value("${broker.key.pedido.notificacao.event}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publishPedidoNotificacaoEvent(NotificacaoDTO notificacaoDTO){
        log.debug("publishPedidoNotificacaoEvent mensagem : {}, exchange {}, rounting key {}", notificacaoDTO, exchange, routingKey);

        rabbitTemplate.convertAndSend(exchange, routingKey, notificacaoDTO);

        log.debug("publishPedidoNotificacaoEvent enviado com sucesso");
        log.info("publishPedidoNotificacaoEvent enviado com sucesso");
    }
}
