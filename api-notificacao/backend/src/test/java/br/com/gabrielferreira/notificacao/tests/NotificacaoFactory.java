package br.com.gabrielferreira.notificacao.tests;

import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import br.com.gabrielferreira.produtos.commons.enums.EmailTemplateEnum;

import java.util.HashMap;
import java.util.Map;

public class NotificacaoFactory {

    private NotificacaoFactory(){}

    public static NotificacaoDTO criarNotificacaoEmailDto(){
        Map<String, Object> dados = new HashMap<>();
        dados.put("descricao", "Seu pedido já foi encaminhado");
        dados.put("codigoPedido", 2L);
        dados.put("dataPedido", "29/05/2024 10:00:00");

        return new NotificacaoDTO("Gabriel Ferreira", "Pedido aberto", new String[]{"teste@email.com.br"}, EmailTemplateEnum.PEDIDOS, dados);
    }
}
