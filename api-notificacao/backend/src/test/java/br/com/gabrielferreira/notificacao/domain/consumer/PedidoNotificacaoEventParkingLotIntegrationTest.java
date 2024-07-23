package br.com.gabrielferreira.notificacao.domain.consumer;

import br.com.gabrielferreira.notificacao.ContainerTest;
import br.com.gabrielferreira.notificacao.domain.model.enums.NotificacaoStatusEnum;
import br.com.gabrielferreira.notificacao.domain.service.NotificacaoService;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import org.junit.jupiter.api.*;
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
@ContainerTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PedidoNotificacaoEventParkingLotIntegrationTest {

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    @MockBean
    private NotificacaoService notificacaoService;

    private NotificacaoDTO notificacaoDTO;

    @BeforeEach
    void setUp(){
        notificacaoDTO = criarNotificacaoEmailDto();
    }

    @Test
    @DisplayName("Deve consumir notificação parking lot")
    @Order(1)
    void deveConsumirNotificacaoParkingLot(){
        assertDoesNotThrow(() -> rabbitTemplate.convertAndSend("ms.produto.notificacaoevent.queue.pl", notificacaoDTO));

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(notificacaoService).salvarNotificacao(any(NotificacaoDTO.class), any(NotificacaoStatusEnum.class)));
    }
}
