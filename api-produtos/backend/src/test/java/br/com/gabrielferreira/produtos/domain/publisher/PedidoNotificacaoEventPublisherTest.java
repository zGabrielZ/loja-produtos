package br.com.gabrielferreira.produtos.domain.publisher;

import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PedidoNotificacaoEventPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private String exchange;

    private String routingKey;

    @InjectMocks
    private PedidoNotificacaoEventPublisher pedidoNotificacaoEventPublisher;

    @BeforeEach
    void setUp(){
        exchange = "ex.teste";
        routingKey = "rk.teste";
    }

    @Test
    @DisplayName("Deve publicar notificação")
    @Order(1)
    void devePublicarNotificacao(){
        NotificacaoDTO notificacaoDTO = new NotificacaoDTO();
        doNothing().when(rabbitTemplate).convertAndSend(exchange, routingKey, notificacaoDTO);

        pedidoNotificacaoEventPublisher.publishPedidoNotificacaoEvent(notificacaoDTO);
    }
}
