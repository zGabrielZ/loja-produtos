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
class HistoricoProdutoControllerIntegrationTest {

    private static final String URL = "/produtos";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON;

    @Autowired
    protected MockMvc mockMvc;

    private Long idHistoricoProdutoExistente;

    private Long idHistoricoProdutoInexsitente;

    private Long idProdutoExistente;

    private Long idProdutoInexsitente;

    @BeforeEach
    void setUp(){
        idProdutoExistente = 1L;
        idHistoricoProdutoExistente = 1L;
        idProdutoInexsitente = -1L;
        idHistoricoProdutoInexsitente = -1L;
    }

    @Test
    @DisplayName("Deve buscar historico produto por id quando existir dado informado")
    @Order(1)
    void deveBuscarHistoricoProdutoPorId() throws Exception {
        String url = URL.concat("/").concat(idProdutoExistente.toString()).concat("/historicos/").concat(idHistoricoProdutoExistente.toString());

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.nome").exists());
        resultActions.andExpect(jsonPath("$.preco").exists());
        resultActions.andExpect(jsonPath("$.dataInclusao").exists());
    }

    @Test
    @DisplayName("Não deve buscar historico produto por id quando não existir produto informado")
    @Order(2)
    void naoDeveBuscarProdutoPorId() throws Exception {
        String url = URL.concat("/").concat(idProdutoInexsitente.toString()).concat("/historicos/").concat(idHistoricoProdutoInexsitente.toString());

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.mensagem").value("Produto não encontrado"));
    }

    @Test
    @DisplayName("Deve buscar historico produtos paginados")
    @Order(3)
    void deveBuscarHistoricoProdutosPaginados() throws Exception {
        String url = URL.concat("/").concat(idProdutoExistente.toString()).concat("/historicos")
                .concat("?page=0&size=5&sort=id,desc");

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.content").exists());
    }

    @Test
    @DisplayName("Não deve buscar historico produto por id quando não existir dado informado")
    @Order(4)
    void naoDeveBuscarHistoricoProdutoPorId() throws Exception {
        String url = URL.concat("/").concat(idProdutoExistente.toString()).concat("/historicos/").concat(idHistoricoProdutoInexsitente.toString());

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.mensagem").value("Histórico produto não encontrado"));
    }
}
