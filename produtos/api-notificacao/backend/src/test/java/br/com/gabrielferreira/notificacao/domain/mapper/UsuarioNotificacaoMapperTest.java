package br.com.gabrielferreira.notificacao.domain.mapper;

import br.com.gabrielferreira.notificacao.domain.model.UsuarioNotificacao;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static br.com.gabrielferreira.notificacao.tests.NotificacaoFactory.criarNotificacaoEmailDto;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioNotificacaoMapperTest {

    private UsuarioNotificacaoMapper usuarioNotificacaoMapper = UsuarioNotificacaoMapper.INSTANCE;

    private NotificacaoMapper notificacaoMapper = NotificacaoMapper.INSTANCE;

    @Test
    @DisplayName("Deve criar usuário notificação")
    @Order(1)
    void deveCriarUsuarioNotificacao(){
        NotificacaoDTO notificacaoDTO = criarNotificacaoEmailDto();

        List<UsuarioNotificacao> usuarioNotificacaos = usuarioNotificacaoMapper.toUsuariosNotificacoes(notificacaoDTO.getDestinatarios(), notificacaoMapper.toNotificacao(criarNotificacaoEmailDto()));

        assertFalse(usuarioNotificacaos.isEmpty());
    }

}
