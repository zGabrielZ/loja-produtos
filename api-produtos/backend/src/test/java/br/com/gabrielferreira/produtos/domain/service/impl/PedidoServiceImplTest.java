package br.com.gabrielferreira.produtos.domain.service.impl;

import br.com.gabrielferreira.produtos.domain.model.Pedido;
import br.com.gabrielferreira.produtos.domain.model.Produto;
import br.com.gabrielferreira.produtos.domain.model.Usuario;
import br.com.gabrielferreira.produtos.domain.model.enums.PedidoStatusEnum;
import br.com.gabrielferreira.produtos.domain.publisher.PedidoNotificacaoEventPublisher;
import br.com.gabrielferreira.produtos.domain.repository.PedidoRepository;
import br.com.gabrielferreira.produtos.domain.service.ProdutoService;
import br.com.gabrielferreira.produtos.domain.service.UsuarioService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static br.com.gabrielferreira.produtos.tests.UsuarioFactory.*;
import static br.com.gabrielferreira.produtos.tests.ProdutoFactory.*;
import static br.com.gabrielferreira.produtos.tests.PedidoFactory.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private PedidoNotificacaoEventPublisher pedidoNotificacaoEventPublisher;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Long idUsuario;

    private Pedido pedido;

    private Long idPedido;

    @Captor
    private ArgumentCaptor<Pedido> argumentCaptorPedidoFinalizado;

    @Captor
    private ArgumentCaptor<Pedido> argumentCaptorPedidoCancelado;

    @BeforeEach
    void setUp(){
        idUsuario = 1L;
        Usuario usuario = criarUsuarioModel(idUsuario);

        Long idProdutoLaranja = 1L;
        Produto produtoLaranja = criarProdutoLaranja(idProdutoLaranja);

        Long idProdutoLimao = 2L;
        Produto produtoLimao = criarProdutoLimao(idProdutoLimao);

        idPedido = 1L;
        pedido = criarPedidoModel();
        Pedido pedidoAposSalvar = criarPedidoModelAposSalvar();
        pedidoAposSalvar.setUsuario(usuario);

        when(usuarioService.buscarUsuarioPorId(idUsuario)).thenReturn(usuario);
        when(produtoService.buscarProdutoPorId(idProdutoLaranja)).thenReturn(produtoLaranja);
        when(produtoService.buscarProdutoPorId(idProdutoLimao)).thenReturn(produtoLimao);
        when(pedidoRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(pedidoRepository.buscarPedido(idUsuario, idPedido)).thenReturn(Optional.of(pedidoAposSalvar));
        doNothing().when(pedidoNotificacaoEventPublisher).publishPedidoNotificacaoEvent(any());
    }

    @Test
    @DisplayName("Deve salvar pedido quando informar campos")
    @Order(1)
    void deveSalvarPedido(){
        Pedido pedidoResultado = pedidoService.salvarPedidoEnviarNotificacao(idUsuario, pedido);

        assertNotNull(pedidoResultado);
        assertEquals("Teste 123", pedidoResultado.getUsuario().getNome());
        assertEquals(BigDecimal.TEN, pedidoResultado.getPrecoTotal());
        assertEquals(PedidoStatusEnum.ABERTO, pedidoResultado.getPedidoStatus());
        assertNotNull(pedidoResultado.getData());
    }

    @Test
    @DisplayName("Deve finalizar pedido")
    @Order(2)
    void deveFinalizarPedido(){
        pedidoService.finalizarPedidoPorIdEnviarNotificacao(idUsuario, idPedido);

        verify(pedidoRepository).save(argumentCaptorPedidoFinalizado.capture());

        Pedido pedidoFinalizadoResultado = argumentCaptorPedidoFinalizado.getValue();

        assertNotNull(pedidoFinalizadoResultado);
        assertEquals(PedidoStatusEnum.FINALIZADO, pedidoFinalizadoResultado.getPedidoStatus());
        assertNotNull(pedidoFinalizadoResultado.getDataFinalizado());
    }

    @Test
    @DisplayName("Deve cancelar pedido")
    @Order(3)
    void deveCancelarPedido(){
        pedidoService.cancelarPedidoPorIdEnviarNotificacao(idUsuario, idPedido);

        verify(pedidoRepository).save(argumentCaptorPedidoCancelado.capture());

        Pedido pedidoCanceladoResultado = argumentCaptorPedidoCancelado.getValue();

        assertNotNull(pedidoCanceladoResultado);
        assertEquals(PedidoStatusEnum.CANCELADO, pedidoCanceladoResultado.getPedidoStatus());
        assertNotNull(pedidoCanceladoResultado.getDataCancelado());
    }
}
