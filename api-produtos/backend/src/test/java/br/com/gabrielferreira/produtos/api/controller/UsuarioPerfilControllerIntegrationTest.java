package br.com.gabrielferreira.produtos.api.controller;

import br.com.gabrielferreira.produtos.utils.TokenUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioPerfilControllerIntegrationTest {

    private static final String URL = "/usuarios";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON;
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected TokenUtils tokenUtils;

    private Long idPerfilExistente;

    private Long idPerfilInexsitente;

    private Long idUsuarioAdminExistente;

    private Long idUsuarioInexsitente;

    private String tokenAdmin;

    @BeforeEach
    void setUp(){
        idPerfilExistente = 1L;
        idUsuarioAdminExistente = 1L;
        idPerfilInexsitente = -1L;
        idUsuarioInexsitente = -1L;
        tokenAdmin = tokenUtils.gerarToken(mockMvc, "teste111@email.com.br", "@Aa123");
    }

    @Test
    @DisplayName("Deve buscar perfis de usuário por id quando existir dado informado")
    @Order(1)
    void deveBuscarPerfilDeUsuarioPorId() throws Exception {
        String url = URL.concat("/").concat(idUsuarioAdminExistente.toString()).concat("/perfis/").concat(idPerfilExistente.toString());

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .header(AUTHORIZATION, BEARER + tokenAdmin)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.descricao").exists());
        resultActions.andExpect(jsonPath("$.autoriedade").exists());
    }

    @Test
    @DisplayName("Não deve buscar perfis de usuário por id quando não existir dado informado")
    @Order(2)
    void naoDeveBuscarPerfilDeUsuarioPorId() throws Exception {
        String url = URL.concat("/").concat(idUsuarioInexsitente.toString()).concat("/perfis/").concat(idPerfilInexsitente.toString());

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .header(AUTHORIZATION, BEARER + tokenAdmin)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.mensagem").value("Usuário não encontrado"));
    }

    @Test
    @DisplayName("Deve buscar perfis de usuários")
    @Order(3)
    void deveBuscarPerfisDeUsuarios() throws Exception {
        String url = URL.concat("/").concat(idUsuarioAdminExistente.toString()).concat("/perfis");

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .header(AUTHORIZATION, BEARER + tokenAdmin)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());

        String conteudo = resultActions.andReturn().getResponse().getContentAsString();
        assertNotEquals("[]", conteudo);
    }

    @Test
    @DisplayName("Não deve buscar perfis de usuário por id quando não existir dado informado")
    @Order(4)
    void naoDeveBuscarPerfilDeUsuarioPorIdPerfil() throws Exception {
        String url = URL.concat("/").concat(idUsuarioAdminExistente.toString()).concat("/perfis/").concat(idPerfilInexsitente.toString());

        ResultActions resultActions = mockMvc
                .perform(get(url)
                        .header(AUTHORIZATION, BEARER + tokenAdmin)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.mensagem").value("Perfil não encontrado"));
    }
}
