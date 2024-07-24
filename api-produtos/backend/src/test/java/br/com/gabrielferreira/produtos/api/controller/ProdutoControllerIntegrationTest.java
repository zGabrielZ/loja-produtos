package br.com.gabrielferreira.produtos.api.controller;

import br.com.gabrielferreira.produtos.api.dto.create.ProdutoCreateDTO;
import br.com.gabrielferreira.produtos.api.dto.update.ProdutoUpdateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static br.com.gabrielferreira.produtos.tests.ProdutoFactory.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProdutoControllerIntegrationTest {

    private static final String URL = "/produtos";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    private ProdutoCreateDTO produtoCreateDTO;

    private Long idProdutoExistente;

    private Long idProdutoInexsitente;

    private Long idProdutoIndependenteExistente;

    private ProdutoUpdateDTO produtoUpdateDTO;

    @BeforeEach
    void setUp(){
        produtoCreateDTO = criarProduto();
        idProdutoExistente = 1L;
        idProdutoInexsitente = -1L;
        produtoUpdateDTO = atualizarProduto();
        idProdutoIndependenteExistente = 3L;
    }

    @Test
    @DisplayName("Deve cadastrar produto quando informar dados")
    @Order(1)
    void deveCadastrarProdutoQuandoInformarDados() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(produtoCreateDTO);

        String nomeEsperado = produtoCreateDTO.getNome();
        String precoEsperado = produtoCreateDTO.getPreco().toString();

        ResultActions resultActions = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.nome").value(nomeEsperado));
        resultActions.andExpect(jsonPath("$.preco").value(precoEsperado));
        resultActions.andExpect(jsonPath("$.dataInclusao").exists());
    }

    @Test
    @DisplayName("Não deve cadastrar produto quando não informar dados")
    @Order(2)
    void naoDeveCadastrarProdutoQuandoNaoInformarDados() throws Exception{
        produtoCreateDTO.setNome(null);
        produtoCreateDTO.setPreco(null);

        String jsonBody = objectMapper.writeValueAsString(produtoCreateDTO);

        ResultActions resultActions = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.titulo").value("Erro validação de campos"));
        resultActions.andExpect(jsonPath("$.mensagem").value("Ocorreu um erro de validação nos campos"));
        resultActions.andExpect(jsonPath("$.campos").exists());
    }

    @Test
    @DisplayName("Não deve cadastrar produto quando informar nome existente")
    @Order(3)
    void naoDeveCadastrarProdutoQuandoInformarNomeExistente() throws Exception{
        produtoCreateDTO.setNome("Pera");

        String jsonBody = objectMapper.writeValueAsString(produtoCreateDTO);

        ResultActions resultActions = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.titulo").value("Regra de negócio"));
        resultActions.andExpect(jsonPath("$.mensagem").value("Não vai ser possível cadastrar este produto pois o nome 'Pera' já foi cadastrado"));
    }

    @Test
    @DisplayName("Deve buscar produto por id quando existir dado informado")
    @Order(4)
    void deveBuscarProdutoPorId() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(get(URL.concat("/{id}"), idProdutoExistente)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.nome").exists());
        resultActions.andExpect(jsonPath("$.preco").exists());
        resultActions.andExpect(jsonPath("$.dataInclusao").exists());
    }

    @Test
    @DisplayName("Não deve buscar produto por id quando não existir dado informado")
    @Order(5)
    void naoDeveBuscarProdutoPorId() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(get(URL.concat("/{id}"), idProdutoInexsitente)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.mensagem").value("Produto não encontrado"));
    }

    @Test
    @DisplayName("Deve atualizar produto quando informar dados")
    @Order(6)
    void deveAtualizarProdutoQuandoInformarDados() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(produtoUpdateDTO);

        Long idEsperado = idProdutoExistente;
        String nomeEsperado = produtoUpdateDTO.getNome();
        String precoEsperado = produtoUpdateDTO.getPreco().toString();

        ResultActions resultActions = mockMvc
                .perform(put(URL.concat("/{id}"), idProdutoExistente)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(idEsperado));
        resultActions.andExpect(jsonPath("$.nome").value(nomeEsperado));
        resultActions.andExpect(jsonPath("$.preco").value(precoEsperado));
        resultActions.andExpect(jsonPath("$.dataInclusao").exists());
    }

    @Test
    @DisplayName("Não deve atualizar produto quando informar nome já existente")
    @Order(7)
    void naoDeveAtualizarProdutoQuandoInformarNomeJaExistente() throws Exception{
        produtoUpdateDTO.setNome("Mexerica");
        String jsonBody = objectMapper.writeValueAsString(produtoUpdateDTO);

        ResultActions resultActions = mockMvc
                .perform(put(URL.concat("/{id}"), idProdutoExistente)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.mensagem").value("Não vai ser possível cadastrar este produto pois o nome 'Mexerica' já foi cadastrado"));
    }

    @Test
    @DisplayName("Deve deletar produto quando existir dados")
    @Order(8)
    void deveDeletarProdutoQuandoExistirDados() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(delete(URL.concat("/{id}"), idProdutoIndependenteExistente)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Não deve deletar produto quando não existir dados")
    @Order(9)
    void naoDeveDeletarProduto() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(delete(URL.concat("/{id}"), idProdutoInexsitente)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.mensagem").value("Produto não encontrado"));
    }

    @Test
    @DisplayName("Deve buscar produtos paginados")
    @Order(10)
    void deveBuscarProdutosPaginados() throws Exception {
        String filtro = URL.concat("?page=0&size=5&sort=id,desc&nome=M&preco=2");

        ResultActions resultActions = mockMvc
                .perform(get(filtro)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.content").exists());
    }

    @Test
    @DisplayName("Não deve deletar produto quando existir relacionamento com pedido")
    @Order(11)
    @Transactional(propagation = Propagation.NEVER)
    void naoDeveDeletarProdutoQuandoExistirRelacionamentoComPedido() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(delete(URL.concat("/{id}"), idProdutoExistente)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.mensagem").value("Esta entidade possui relacionamento"));
    }
}
