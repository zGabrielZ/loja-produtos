package br.com.gabrielferreira.notificacao.domain.publisher;

import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.gabrielferreira.notificacao.tests.NotificacaoFactory.criarNotificacaoEmailDto;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PedidoNotificacaoEventParkingLotPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PedidoNotificacaoEventParkingLotPublisher pedidoNotificacaoEventParkingLotPublisher;

    private NotificacaoDTO notificacao;

    @BeforeEach
    void setUp(){
        notificacao = criarNotificacaoEmailDto();
    }

    @Test
    @DisplayName("Deve enviar pedido notificação parking lot")
    @Order(1)
    void deveEnviarPedidoNotificacao(){
        pedidoNotificacaoEventParkingLotPublisher.publishPedidoNotificacaoEventParkingLot(notificacao);

        verify(rabbitTemplate).convertAndSend(isNull(), isNull(), any(NotificacaoDTO.class));
    }
}
