package br.com.gabrielferreira.notificacao.domain.service.impl;

import br.com.gabrielferreira.notificacao.domain.mapper.NotificacaoMapper;
import br.com.gabrielferreira.notificacao.domain.mapper.UsuarioNotificacaoMapper;
import br.com.gabrielferreira.notificacao.domain.model.Notificacao;
import br.com.gabrielferreira.notificacao.domain.repository.NotificacaoRepository;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static br.com.gabrielferreira.notificacao.tests.NotificacaoFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotificacaoServiceImplTest {

    private NotificacaoServiceImpl notificacaoService;

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @Mock
    private NotificacaoMapper notificacaoMapper;

    @Mock
    private UsuarioNotificacaoMapper usuarioNotificacaoMapper;

    private NotificacaoDTO notificacao;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        notificacaoService = new NotificacaoServiceImpl(new LogEmailServiceImpl(), notificacaoRepository, notificacaoMapper, usuarioNotificacaoMapper);

        notificacao = criarNotificacaoEmailDto();
    }

    @Test
    @DisplayName("Deve enviar notificação")
    @Order(1)
    void deveEnviarNotificacao(){
        when(notificacaoMapper.toNotificacao(any())).thenReturn(mock(Notificacao.class));
        when(usuarioNotificacaoMapper.toUsuariosNotificacoes(any(), any())).thenReturn(new ArrayList<>());
        when(notificacaoRepository.save(any())).thenReturn(mock(Notificacao.class));

        NotificacaoDTO notificacaoRetorno = notificacaoService.enviarNotificacao(notificacao);

        assertNotNull(notificacaoRetorno);
        assertEquals("Pedido aberto", notificacaoRetorno.getTitulo());
        verify(notificacaoRepository, times(1)).save(any());
    }

}
