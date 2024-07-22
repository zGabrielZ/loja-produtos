package br.com.gabrielferreira.notificacao.api.controller;

import br.com.gabrielferreira.notificacao.ContainerTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContainerTest
class NotificacaoControllerIntegrationTest {

    private static final String URL = "/notificacoes";

    private static final MediaType MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    @DisplayName("Deve buscar notificações paginados")
    @Order(1)
    void deveBuscarNotificacoesPaginados() throws Exception {
        String url = URL.concat("?page=0&size=5&sort=id,desc&titulo=Teste");

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.content").exists());
    }

    @Test
    @DisplayName("Não deve buscar notificações paginados quando ocorrer erro")
    @Order(12)
    void naoDeveBuscarNotificacoesPaginados() throws Exception {
        String url = URL.concat("?page=0&size=5&sort=id,desc&status=SUCESSO1");

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isInternalServerError());
        resultActions.andExpect(jsonPath("$.titulo").value("Erro inesperado"));
    }
}
