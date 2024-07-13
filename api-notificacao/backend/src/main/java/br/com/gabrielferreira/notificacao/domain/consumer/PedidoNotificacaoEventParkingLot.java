package br.com.gabrielferreira.notificacao.domain.consumer;

import br.com.gabrielferreira.notificacao.domain.model.enums.NotificacaoStatusEnum;
import br.com.gabrielferreira.notificacao.domain.service.NotificacaoService;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class PedidoNotificacaoEventParkingLot {

    private final NotificacaoService notificacaoService;

    @RabbitListener(queues = "${broker.queue.pedido.notificacao.pl}")
    public void listenPedidoNotificacaoParkingLot(@Payload NotificacaoDTO notificacaoDTO){
        log.debug("listenPedidoNotificacaoParkingLot notificação recebida : {}", notificacaoDTO);
        notificacaoDTO = notificacaoService.salvarNotificacao(notificacaoDTO, NotificacaoStatusEnum.FALHO);
        log.debug("Mensagem salva com sucesso {}", notificacaoDTO);
        log.info("Mensagem salva com sucesso");
    }
}
