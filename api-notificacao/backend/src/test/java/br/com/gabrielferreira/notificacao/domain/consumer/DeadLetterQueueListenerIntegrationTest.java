package br.com.gabrielferreira.notificacao.domain.consumer;

import br.com.gabrielferreira.notificacao.AbstractIntegrationTest;
import br.com.gabrielferreira.notificacao.domain.publisher.PedidoNotificacaoEventDeadLetterQueuePublisher;
import br.com.gabrielferreira.notificacao.domain.publisher.PedidoNotificacaoEventParkingLotPublisher;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import org.junit.jupiter.api.*;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static br.com.gabrielferreira.notificacao.tests.NotificacaoFactory.criarNotificacaoEmailDto;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.awaitility.Awaitility.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeadLetterQueueListenerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    @MockBean
    private PedidoNotificacaoEventDeadLetterQueuePublisher pedidoNotificacaoEventDeadLetterQueuePublisher;

    @MockBean
    private PedidoNotificacaoEventParkingLotPublisher pedidoNotificacaoEventParkingLotPublisher;

    private NotificacaoDTO notificacaoDTO;

    @BeforeEach
    void setUp(){
        notificacaoDTO = criarNotificacaoEmailDto();
    }

    @Test
    @DisplayName("Deve consumir notificação dead letter queue")
    @Order(1)
    void deveConsumirNotificacaoDeadLetterQueue(){
        assertDoesNotThrow(() -> rabbitTemplate.convertAndSend("ms.produto.notificacaoevent.queue.dlq", notificacaoDTO));

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(pedidoNotificacaoEventDeadLetterQueuePublisher)
                        .publishPedidoNotificacaoEventDeadLetterQueue(any(NotificacaoDTO.class), any(MessagePostProcessor.class)));
    }

    @Test
    @DisplayName("Deve consumir notificação dead letter queue parking lot")
    @Order(2)
    void deveConsumirNotificacaoDeadLetterQueueParkingLot(){
        assertDoesNotThrow(() -> rabbitTemplate.convertAndSend("ms.produto.notificacaoevent.queue.dlq", notificacaoDTO, message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setHeader("x-dlq-retry", 4);
            return message;
        }));

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(pedidoNotificacaoEventParkingLotPublisher)
                        .publishPedidoNotificacaoEventParkingLot(any(NotificacaoDTO.class)));
    }
}
