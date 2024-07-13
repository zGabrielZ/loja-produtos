package br.com.gabrielferreira.notificacao.domain.publisher;

import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.gabrielferreira.notificacao.tests.NotificacaoFactory.criarNotificacaoEmailDto;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PedidoNotificacaoEventDeadLetterQueuePublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PedidoNotificacaoEventDeadLetterQueuePublisher pedidoNotificacaoEventDeadLetterQueuePublisher;

    private NotificacaoDTO notificacao;

    @BeforeEach
    void setUp(){
        notificacao = criarNotificacaoEmailDto();
    }

    @Test
    @DisplayName("Deve enviar pedido notificação dead letter queue com message post")
    @Order(1)
    void deveEnviarPedidoNotificacaoDeadLetterQueueMessagePost(){
        MessagePostProcessor messagePostProcessor = message -> message;

        pedidoNotificacaoEventDeadLetterQueuePublisher.publishPedidoNotificacaoEventDeadLetterQueue(notificacao, messagePostProcessor);

        verify(rabbitTemplate).convertAndSend(isNull(), isNull(), any(NotificacaoDTO.class), any(MessagePostProcessor.class));
    }

    @Test
    @DisplayName("Deve enviar pedido notificação dead letter queue")
    @Order(2)
    void deveEnviarPedidoNotificacaoDeadLetterQueue(){
        pedidoNotificacaoEventDeadLetterQueuePublisher.publishPedidoNotificacaoEventDeadLetterQueue(notificacao);

        verify(rabbitTemplate).convertAndSend(isNull(), isNull(), any(NotificacaoDTO.class));
    }
}
