package br.com.gabrielferreira.notificacao.domain.consumer;

import br.com.gabrielferreira.notificacao.domain.publisher.PedidoNotificacaoEventDeadLetterQueuePublisher;
import br.com.gabrielferreira.notificacao.domain.service.impl.NotificacaoServiceImpl;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.gabrielferreira.notificacao.tests.NotificacaoFactory.criarNotificacaoEmailDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PedidoNotificacaoEventConsumerTest {

    @Mock
    private NotificacaoServiceImpl notificacaoService;

    @Mock
    private PedidoNotificacaoEventDeadLetterQueuePublisher pedidoNotificacaoEventDeadLetterQueuePublisher;

    @InjectMocks
    private PedidoNotificacaoEventConsumer pedidoNotificacaoEventConsumer;

    private NotificacaoDTO notificacaoDTO;

    @Captor
    private ArgumentCaptor<NotificacaoDTO> argumentCaptorNotificacao;

    @BeforeEach
    void setUp(){
        notificacaoDTO = criarNotificacaoEmailDto();
    }

    @Test
    @DisplayName("Deve consumir notificação")
    @Order(1)
    void deveConsumirNotificacao(){
        when(notificacaoService.enviarNotificacao(any())).thenReturn(notificacaoDTO);

        pedidoNotificacaoEventConsumer.listenPedidoNotificacaoEvent(notificacaoDTO);

        verify(notificacaoService).enviarNotificacao(argumentCaptorNotificacao.capture());

        NotificacaoDTO notificacao = argumentCaptorNotificacao.getValue();
        assertNotNull(notificacao);
    }

    @Test
    @DisplayName("Deve consumir notificação dead letter queue")
    @Order(2)
    void deveConsumirNotificacaoDeadLetterQueue(){
        doThrow(RuntimeException.class).when(notificacaoService).enviarNotificacao(notificacaoDTO);

        pedidoNotificacaoEventConsumer.listenPedidoNotificacaoEvent(notificacaoDTO);

        verify(pedidoNotificacaoEventDeadLetterQueuePublisher).publishPedidoNotificacaoEventDeadLetterQueue(any(NotificacaoDTO.class));
    }
}
