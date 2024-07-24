package br.com.gabrielferreira.produtos.api.controller;

import br.com.gabrielferreira.produtos.api.dto.create.UsuarioCreateDTO;
import br.com.gabrielferreira.produtos.api.dto.update.UsuarioSenhaUpdateDTO;
import br.com.gabrielferreira.produtos.api.dto.update.UsuarioUpdateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static br.com.gabrielferreira.produtos.tests.UsuarioFactory.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioControllerIntegrationTest {

    private static final String URL = "/usuarios";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON;
    private static final String SENHA_SEM_CARACTERE_ESPECIAL = "Teste";
    private static final String SENHA_SEM_CARACTERE_MAIUSCULA = "@teste";
    private static final String SENHA_SEM_CARACTERE_MINUSCULA = "@TESTE";
    private static final String SENHA_SEM_CARACTERE_DIGITO = "@TESTe";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    private UsuarioCreateDTO usuarioCreateDTO;

    private Long idUsuarioExistente;

    private Long idUsuarioInexistente;

    private UsuarioUpdateDTO usuarioUpdateDTO;

    private UsuarioSenhaUpdateDTO usuarioSenhaUpdateDTO;

    @BeforeEach
    void setUp(){
        usuarioCreateDTO = criarUsuario();
        idUsuarioExistente = 1L;
        idUsuarioInexistente = -1L;
        usuarioUpdateDTO = atualizarUsuario();
        usuarioSenhaUpdateDTO = atualizarSenhaUsuario();
    }

    @Test
    @DisplayName("Deve cadastrar usuário quando informar dados")
    @Order(1)
    void deveCadastrarUsuarioQuandoInformarDados() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(usuarioCreateDTO);

        String nomeEsperado = usuarioCreateDTO.getNome();
        String emailEsperado = usuarioCreateDTO.getEmail();

        ResultActions resultActions = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.nome").value(nomeEsperado));
        resultActions.andExpect(jsonPath("$.email").value(emailEsperado));
        resultActions.andExpect(jsonPath("$.dataInclusao").exists());
        resultActions.andExpect(jsonPath("$.perfis").exists());
    }

    @Test
    @DisplayName("Não deve cadastrar usuário quando não informar senha sem caractere especial")
    @Order(2)
    void naoDeveCadastrarUsuarioQuandoInformarSenhaSemCaractereEspecial() throws Exception{
        usuarioCreateDTO.setSenha(SENHA_SEM_CARACTERE_ESPECIAL);
        String jsonBody = objectMapper.writeValueAsString(usuarioCreateDTO);

        ResultActions resultActions = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.titulo").value("Regra de negócio"));
        resultActions.andExpect(jsonPath("$.mensagem").value("A senha informada tem que ter pelo menos uma caractere especial"));
    }

    @Test
    @DisplayName("Não deve cadastrar usuário quando não informar senha sem caractere maisuculas")
    @Order(3)
    void naoDeveCadastrarUsuarioQuandoInformarSenhaSemCaractereMaiusculas() throws Exception{
        usuarioCreateDTO.setSenha(SENHA_SEM_CARACTERE_MAIUSCULA);
        String jsonBody = objectMapper.writeValueAsString(usuarioCreateDTO);

        ResultActions resultActions = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.titulo").value("Regra de negócio"));
        resultActions.andExpect(jsonPath("$.mensagem").value("A senha informada tem que ter pelo menos uma caractere maiúsculas"));
    }

    @Test
    @DisplayName("Não deve cadastrar usuário quando não informar senha sem caractere minuscula")
    @Order(4)
    void naoDeveCadastrarUsuarioQuandoInformarSenhaSemCaractereMinuscula() throws Exception{
        usuarioCreateDTO.setSenha(SENHA_SEM_CARACTERE_MINUSCULA);
        String jsonBody = objectMapper.writeValueAsString(usuarioCreateDTO);

        ResultActions resultActions = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.titulo").value("Regra de negócio"));
        resultActions.andExpect(jsonPath("$.mensagem").value("A senha informada tem que ter pelo menos uma caractere minúsculas"));
    }

    @Test
    @DisplayName("Não deve cadastrar usuário quando não informar senha sem caractere digito")
    @Order(5)
    void naoDeveCadastrarUsuarioQuandoInformarSenhaSemCaractereDigito() throws Exception{
        usuarioCreateDTO.setSenha(SENHA_SEM_CARACTERE_DIGITO);
        String jsonBody = objectMapper.writeValueAsString(usuarioCreateDTO);

        ResultActions resultActions = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.titulo").value("Regra de negócio"));
        resultActions.andExpect(jsonPath("$.mensagem").value("A senha informada tem que ter pelo menos um caractere dígito"));
    }

    @Test
    @DisplayName("Não deve cadastrar usuário quando informar email já existente")
    @Order(6)
    void naoDeveCadastrarUsuarioQuandoTiverEmailExistente() throws Exception{
        usuarioCreateDTO.setEmail("teste123@email.com.br");
        String jsonBody = objectMapper.writeValueAsString(usuarioCreateDTO);

        ResultActions resultActions = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.titulo").value("Regra de negócio"));
        resultActions.andExpect(jsonPath("$.mensagem").value("Não vai ser possível cadastrar este usuário pois o e-mail 'teste123@email.com.br' já foi cadastrado"));
    }

    @Test
    @DisplayName("Não deve cadastrar usuário quando informar perfil não existente")
    @Order(7)
    void naoDeveCadastrarUsuarioQuandoInformarPerfilNaoExistente() throws Exception{
        usuarioCreateDTO.getPerfis().get(0).setId(-1L);

        String jsonBody = objectMapper.writeValueAsString(usuarioCreateDTO);

        ResultActions resultActions = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.titulo").value("Não encontrado"));
        resultActions.andExpect(jsonPath("$.mensagem").value("Perfil não encontrado"));
    }

    @Test
    @DisplayName("Não deve cadastrar usuário quando informar perfil duplicado")
    @Order(8)
    void naoDeveCadastrarUsuarioQuandoInformarPerfilDuplicado() throws Exception{
        usuarioCreateDTO.getPerfis().get(0).setId(1L);
        usuarioCreateDTO.getPerfis().get(1).setId(1L);

        String jsonBody = objectMapper.writeValueAsString(usuarioCreateDTO);

        ResultActions resultActions = mockMvc
                .perform(post(URL)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.titulo").value("Regra de negócio"));
        resultActions.andExpect(jsonPath("$.mensagem").value("Não vai ser possível cadastrar este usuário pois tem perfis duplicados ou mais de duplicados"));
    }

    @Test
    @DisplayName("Deve buscar usuário por id quando existir dado informado")
    @Order(9)
    void deveBuscarUsuarioPorId() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(get(URL.concat("/{id}"), idUsuarioExistente)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.nome").exists());
        resultActions.andExpect(jsonPath("$.email").exists());
        resultActions.andExpect(jsonPath("$.dataInclusao").exists());
    }

    @Test
    @DisplayName("Não deve buscar usuário por id quando não existir dado informado")
    @Order(10)
    void naoDeveBuscarUsuarioPorId() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(get(URL.concat("/{id}"), idUsuarioInexistente)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.mensagem").value("Usuário não encontrado"));
    }

    @Test
    @DisplayName("Deve atualizar usuário quando informar dados")
    @Order(11)
    void deveAtualizarUsuarioQuandoInformarDados() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(usuarioUpdateDTO);

        Long idEsperado = idUsuarioExistente;
        String nomeEsperado = usuarioUpdateDTO.getNome();

        ResultActions resultActions = mockMvc
                .perform(put(URL.concat("/{id}"), idUsuarioExistente)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(idEsperado));
        resultActions.andExpect(jsonPath("$.nome").value(nomeEsperado));
    }

    @Test
    @DisplayName("Deve atualizar senha do usuário quando informar dados")
    @Order(12)
    void deveAtualizarSenhaUsuarioQuandoInformarDados() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(usuarioSenhaUpdateDTO);

        Long idEsperado = idUsuarioExistente;

        ResultActions resultActions = mockMvc
                .perform(put(URL.concat("/{id}").concat("/senha"), idUsuarioExistente)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(idEsperado));
    }

    @Test
    @DisplayName("Não deve atualizar senha do usuário quando informar senha antiga diferente da senha cadastrada")
    @Order(13)
    void naoDeveAtualizarSenhaUsuarioQuandoInformarSenhaIncompartivel() throws Exception{
        usuarioSenhaUpdateDTO.setAntigaSenha("@Aa123334556566");
        String jsonBody = objectMapper.writeValueAsString(usuarioSenhaUpdateDTO);

        ResultActions resultActions = mockMvc
                .perform(put(URL.concat("/{id}").concat("/senha"), idUsuarioExistente)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.titulo").value("Regra de negócio"));
        resultActions.andExpect(jsonPath("$.mensagem").value("Senha antiga informada é incompatível"));
    }

    @Test
    @DisplayName("Não deve atualizar senha do usuário quando informar nova senha igual da senha cadastrada")
    @Order(14)
    void naoDeveAtualizarSenhaUsuarioQuandoInformarSenhaIgualCadastrada() throws Exception{
        usuarioSenhaUpdateDTO.setNovaSenha("@Aa123");
        String jsonBody = objectMapper.writeValueAsString(usuarioSenhaUpdateDTO);

        ResultActions resultActions = mockMvc
                .perform(put(URL.concat("/{id}").concat("/senha"), idUsuarioExistente)
                        .content(jsonBody)
                        .contentType(MEDIA_TYPE_JSON)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.titulo").value("Regra de negócio"));
        resultActions.andExpect(jsonPath("$.mensagem").value("Nova senha é igual ao anterior"));
    }

    @Test
    @DisplayName("Deve deletar usuário quando existir dados")
    @Order(15)
    void deveDeletarUsuarioQuandoExistirDados() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(delete(URL.concat("/{id}"), idUsuarioExistente)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Não deve deletar usuário quando não existir dados")
    @Order(16)
    void naoDeveDeletarUsuario() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(delete(URL.concat("/{id}"), idUsuarioInexistente)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.mensagem").value("Usuário não encontrado"));
    }

    @Test
    @DisplayName("Deve buscar usuários paginados")
    @Order(17)
    void deveBuscarUsuariosPaginados() throws Exception {
        String filtro = URL.concat("?page=0&size=5&sort=id,desc&nome=Teste&email=teste123@email.com.br");

        ResultActions resultActions = mockMvc
                .perform(get(filtro)
                        .accept(MEDIA_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.content").exists());
    }
}
