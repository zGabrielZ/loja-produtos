package br.com.gabrielferreira.notificacao.domain.publisher;

import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class PedidoNotificacaoEventParkingLotPublisher {

    private final RabbitTemplate rabbitTemplate;

    private final String exchange;

    private final String routingKey;

    public PedidoNotificacaoEventParkingLotPublisher(RabbitTemplate rabbitTemplate,
                                                     @Value("${broker.exchange.pedido.notificacao.pl}") String exchange,
                                                     @Value("${broker.key.pedido.notificacao.pl}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publishPedidoNotificacaoEventParkingLot(NotificacaoDTO notificacaoDTO){
        log.debug("publishPedidoNotificacaoEventParkingLot mensagem : {}, exchange {}, rounting key {}", notificacaoDTO, exchange, routingKey);

        rabbitTemplate.convertAndSend(exchange, routingKey, notificacaoDTO);

        log.debug("publishPedidoNotificacaoEventParkingLot enviado com sucesso");
        log.info("publishPedidoNotificacaoEventParkingLot enviado com sucesso");
    }
}
