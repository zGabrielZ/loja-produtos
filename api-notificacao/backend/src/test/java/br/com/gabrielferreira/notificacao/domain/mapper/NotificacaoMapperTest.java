package br.com.gabrielferreira.notificacao.domain.mapper;

import br.com.gabrielferreira.notificacao.domain.model.Notificacao;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.gabrielferreira.notificacao.tests.NotificacaoFactory.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotificacaoMapperTest {

    private NotificacaoMapper notificacaoMapper = NotificacaoMapper.INSTANCE;

    @Test
    @DisplayName("Deve criar notificação")
    @Order(1)
    void deveCriarNotificacao(){
        NotificacaoDTO notificacaoDTO = criarNotificacaoEmailDto();

        Notificacao notificacao = notificacaoMapper.toNotificacao(notificacaoDTO);

        assertEquals(notificacaoDTO.getNomeRemetente(), notificacao.getNomeRemetente());
        assertEquals(notificacaoDTO.getTitulo(), notificacao.getTitulo());
        assertEquals(notificacaoDTO.getEmailTemplate(), notificacao.getEmailTemplate());
    }

}
