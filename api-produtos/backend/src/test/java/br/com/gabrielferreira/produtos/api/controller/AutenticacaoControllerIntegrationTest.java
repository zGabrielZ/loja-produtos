package br.com.gabrielferreira.produtos.api.controller;

import br.com.gabrielferreira.produtos.api.dto.AuthDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AutenticacaoControllerIntegrationTest {

    private static final String URL = "/auth";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON;
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    private AuthDTO authDTO;

    @BeforeEach
    void setUp(){
        authDTO = AuthDTO.builder()
                .email("teste111@email.com.br")
                .senha("@Aa123")
                .build();
    }

    @Test
    @DisplayName("Deve realizar login quando existir dados")
    @Order(1)
    void deveRealizarLoginQuandoExistirDados() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(authDTO);

        ResultActions resultActions = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(jsonPath("$.tipo").exists());
        resultActions.andExpect(jsonPath("$.tipo").value("Bearer"));
        resultActions.andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Não deve realizar login quando não existir email")
    @Order(2)
    void naoDeveRealizarLoginQuandoNaoExistirEmail() throws Exception{
        authDTO.setEmail("naoexiste@email.com");
        String jsonBody = objectMapper.writeValueAsString(authDTO);

        ResultActions resultActions = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.titulo").value("Não encontrado"));
        resultActions.andExpect(jsonPath("$.mensagem").value("E-mail naoexiste@email.com não encontrado"));
    }

    @Test
    @DisplayName("Não deve realizar login quando senha informada não combinar com a senha já cadastrada")
    @Order(3)
    void naoDeveRealizarLoginQuandoSenhaNaoCombinar() throws Exception{
        authDTO.setSenha("1111111111111");
        String jsonBody = objectMapper.writeValueAsString(authDTO);

        ResultActions resultActions = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.titulo").value("Regra de negócio"));
        resultActions.andExpect(jsonPath("$.mensagem").value("Senha inválida"));
    }

    @Test
    @DisplayName("Deve realizar refresh token do usuário autenticado")
    @Order(4)
    void deveRealizarRefreshTokenUsuarioLogado() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(authDTO);

        ResultActions resultActionsLogin = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        String response = resultActionsLogin.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String token = jsonParser.parseMap(response).get("token").toString();

        ResultActions resultActionsRefresh = mockMvc
                .perform(post(URL.concat("/refresh-token"))
                        .header(AUTHORIZATION, BEARER + token)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActionsRefresh.andExpect(status().isOk());
        resultActionsRefresh.andExpect(jsonPath("$.tipo").exists());
        resultActionsRefresh.andExpect(jsonPath("$.tipo").value("Bearer"));
        resultActionsRefresh.andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Não deve realizar refresh token quando tiver token inválido ou usuário não encontrado")
    @Order(5)
    void naoDeveRealizarRefreshTokenQuandoTokenInvalido() throws Exception{
        ResultActions resultActions = mockMvc
                .perform(post(URL.concat("/refresh-token"))
                        .header(AUTHORIZATION, BEARER+ "123invalido123")
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.titulo").value("Não autorizado"));
        resultActions.andExpect(jsonPath("$.mensagem").value("Você precisa fazer login primeiro para executar esta função"));
    }
}
