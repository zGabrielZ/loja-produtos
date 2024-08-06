package br.com.gabrielferreira.notificacao.api.controller;

import br.com.gabrielferreira.notificacao.AbstractIntegrationTest;
import br.com.gabrielferreira.notificacao.utils.TokenUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotificacaoControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String URL = "/notificacoes";

    private static final MediaType MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON;
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected TokenUtils tokenUtils;

    private String tokenAdmin;

    private String tokenFuncionario;

    private String tokenCliente;

    @BeforeEach
    void setUp(){
        tokenAdmin = tokenUtils.gerarToken(Arrays.asList("ROLE_ADMIN", "ROLE_FUNCIONARIO"));
        tokenFuncionario = tokenUtils.gerarToken(List.of("ROLE_FUNCIONARIO"));
        tokenCliente = tokenUtils.gerarToken(List.of("ROLE_CLIENTE"));
    }

    @Test
    @DisplayName("Deve buscar notificações paginados")
    @Order(1)
    void deveBuscarNotificacoesPaginados() throws Exception {
        String url = URL.concat("?page=0&size=5&sort=id,desc&titulo=Teste");

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .header(AUTHORIZATION, BEARER + tokenAdmin)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.content").exists());
    }

    @Test
    @DisplayName("Não deve buscar notificações paginados quando ocorrer erro")
    @Order(2)
    void naoDeveBuscarNotificacoesPaginados() throws Exception {
        String url = URL.concat("?page=0&size=5&sort=id,desc&status=SUCESSO1");

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .header(AUTHORIZATION, BEARER + tokenAdmin)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isInternalServerError());
        resultActions.andExpect(jsonPath("$.titulo").value("Erro inesperado"));
    }

    @Test
    @DisplayName("Não deve buscar notificações paginados quando for funcionário")
    @Order(3)
    void naoDeveBuscarNotificacoesPaginadosQuandoNaoTiverPermissaoFuncionario() throws Exception {
        String url = URL.concat("?page=0&size=5&sort=id,desc&titulo=Teste");

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .header(AUTHORIZATION, BEARER + tokenFuncionario)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isForbidden());
        resultActions.andExpect(jsonPath("$.mensagem").value("Você não tem a permissão de realizar esta ação"));
    }

    @Test
    @DisplayName("Não deve buscar notificações paginados quando for cliente")
    @Order(4)
    void naoDeveBuscarNotificacoesPaginadosQuandoNaoTiverPermissaoCliente() throws Exception {
        String url = URL.concat("?page=0&size=5&sort=id,desc&titulo=Teste");

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .header(AUTHORIZATION, BEARER + tokenCliente)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isForbidden());
        resultActions.andExpect(jsonPath("$.mensagem").value("Você não tem a permissão de realizar esta ação"));
    }
}
