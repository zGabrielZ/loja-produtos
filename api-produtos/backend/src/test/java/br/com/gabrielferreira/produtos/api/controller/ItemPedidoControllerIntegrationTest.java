package br.com.gabrielferreira.produtos.api.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemPedidoControllerIntegrationTest {

    private static final String URL = "/usuarios";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON;

    @Autowired
    protected MockMvc mockMvc;

    private Long idUsuarioExistente;

    private Long idPedidoExistente;

    private Long idPedidoInexistente;

    private Long idItemPedidoExistente;

    private Long idItemPedidoInexistente;

    @BeforeEach
    void setUp(){
        idUsuarioExistente = 1L;
        idPedidoExistente = 1L;
        idPedidoInexistente = -1L;
        idItemPedidoExistente = 1L;
        idItemPedidoInexistente = -1L;
    }

    @Test
    @DisplayName("Deve buscar item pedido por id quando existir dado informado")
    @Order(1)
    void deveBuscarItemPedidoPorId() throws Exception {
        String url = URL.concat("/").concat(idUsuarioExistente.toString()).concat("/pedidos/")
                .concat(idPedidoExistente.toString()).concat("/itens/").concat(idItemPedidoExistente.toString());

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.quantidade").exists());
        resultActions.andExpect(jsonPath("$.valorAtualProduto").exists());
        resultActions.andExpect(jsonPath("$.subTotal").exists());
        resultActions.andExpect(jsonPath("$.produto").exists());
        resultActions.andExpect(jsonPath("$.usuario").exists());
    }

    @Test
    @DisplayName("Não deve buscar item pedido por id quando não existir dado informado")
    @Order(2)
    void naoDeveBuscarItemPedidoPorId() throws Exception {
        String url = URL.concat("/").concat(idUsuarioExistente.toString()).concat("/pedidos/")
                .concat(idPedidoExistente.toString()).concat("/itens/").concat(idItemPedidoInexistente.toString());

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.mensagem").value("Item pedido não encontrado"));
    }

    @Test
    @DisplayName("Não deve buscar item pedido por id quando não existir usuário informado")
    @Order(3)
    void naoDeveBuscarItemPedidoPorIdQuandoNaoEncontrarUsuarioComPedido() throws Exception {
        String url = URL.concat("/").concat(idUsuarioExistente.toString()).concat("/pedidos/")
                .concat(idPedidoInexistente.toString()).concat("/itens/").concat(idItemPedidoExistente.toString());

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.mensagem").value("Usuário com este pedido não encontrado"));
    }

    @Test
    @DisplayName("Deve buscar item pedidos paginados")
    @Order(4)
    void deveBuscarPedidosPaginados() throws Exception {
        String url = URL.concat("/").concat(idUsuarioExistente.toString()).concat("/pedidos/")
                .concat(idPedidoExistente.toString()).concat("/itens")
                .concat("?page=0&size=5&sort=id,desc");

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.content").exists());
    }
}
