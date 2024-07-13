package br.com.gabrielferreira.produtos.api.controller;

import br.com.gabrielferreira.produtos.api.dto.PedidoDTO;
import br.com.gabrielferreira.produtos.api.dto.create.PedidoCreateDTO;
import br.com.gabrielferreira.produtos.api.mapper.ErroPadraoMapper;
import br.com.gabrielferreira.produtos.api.mapper.PedidoMapper;
import br.com.gabrielferreira.produtos.domain.model.Pedido;
import br.com.gabrielferreira.produtos.domain.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static br.com.gabrielferreira.produtos.tests.PedidoFactory.*;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = PedidoController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PedidoControllerTest {

    private static final String URL = "/usuarios";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected PedidoService pedidoService;

    @MockBean
    protected PedidoMapper pedidoMapper;

    @MockBean
    protected ErroPadraoMapper erroPadraoMapper;

    private PedidoCreateDTO pedidoCreateDTO;

    private Pedido pedidoCriado;

    private Pedido pedidoCriadoAposSalvar;

    private PedidoDTO pedidoDTOCriadoAposSalvar;

    private Long idUsuarioExistente;

    private Long idPedidoExistente;

    @BeforeEach
    void setUp(){
        idUsuarioExistente = 1L;
        idPedidoExistente = 1L;
        pedidoCreateDTO = criarPedido();
        pedidoCriado = criarPedidoModel();
        pedidoCriadoAposSalvar = criarPedidoModelAposSalvar();
        pedidoDTOCriadoAposSalvar = criarPedidoDtoAposSalvar();
    }

    @Test
    @DisplayName("Deve cadastrar pedido quando informar dados")
    @Order(1)
    void deveCadastrarPedidoQuandoInformarDados() throws Exception{
        when(pedidoMapper.toPedido(pedidoCreateDTO)).thenReturn(pedidoCriado);
        when(pedidoService.salvarPedidoEnviarNotificacao(idUsuarioExistente, pedidoCriado)).thenReturn(pedidoCriadoAposSalvar);
        when(pedidoMapper.toPedidoDto(pedidoCriadoAposSalvar)).thenReturn(pedidoDTOCriadoAposSalvar);

        String url = URL.concat("/").concat(idUsuarioExistente.toString()).concat("/pedidos");
        String jsonBody = objectMapper.writeValueAsString(pedidoCreateDTO);

        String pedidoStatusEsperado = "ABERTO";
        String precoTotalEsperado = BigDecimal.valueOf(10).toString();

        ResultActions resultActions = mockMvc
                .perform(post(url)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.data").exists());
        resultActions.andExpect(jsonPath("$.pedidoStatus").value(pedidoStatusEsperado));
        resultActions.andExpect(jsonPath("$.precoTotal").value(precoTotalEsperado));
        resultActions.andExpect(jsonPath("$.dataInclusao").exists());
        resultActions.andExpect(jsonPath("$.itensPedidos").exists());
    }

    @Test
    @DisplayName("Deve finalizar pedido por id")
    @Order(2)
    void deveFinalizarPedidoPorId() throws Exception {
        doNothing().when(pedidoService).finalizarPedidoPorIdEnviarNotificacao(idUsuarioExistente, idPedidoExistente);

        String url = URL.concat("/").concat(idUsuarioExistente.toString()).concat("/pedidos/")
                .concat(idPedidoExistente.toString()).concat("/finalizar");

        ResultActions resultActions = mockMvc
                .perform(put(url)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve cancelar pedido por id")
    @Order(3)
    void deveCancelarPedidoPorId() throws Exception {
        doNothing().when(pedidoService).cancelarPedidoPorIdEnviarNotificacao(idUsuarioExistente, idPedidoExistente);

        String url = URL.concat("/").concat(idUsuarioExistente.toString()).concat("/pedidos/")
                .concat(idPedidoExistente.toString()).concat("/cancelar");

        ResultActions resultActions = mockMvc
                .perform(put(url)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
    }
}
