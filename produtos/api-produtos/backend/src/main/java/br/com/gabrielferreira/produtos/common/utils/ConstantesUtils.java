package br.com.gabrielferreira.produtos.common.utils;

import br.com.gabrielferreira.produtos.commons.dto.ItemPedidoDTO;
import br.com.gabrielferreira.produtos.commons.dto.NotificacaoDTO;
import br.com.gabrielferreira.produtos.commons.dto.PedidoDTO;
import br.com.gabrielferreira.produtos.commons.enums.EmailTemplateEnum;
import br.com.gabrielferreira.produtos.domain.model.Pedido;
import br.com.gabrielferreira.produtos.domain.model.Usuario;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static br.com.gabrielferreira.produtos.common.utils.DataUtils.*;
import static br.com.gabrielferreira.produtos.common.utils.MascarasUtils.*;

public class ConstantesUtils {

    private ConstantesUtils(){}

    private static final String DESCRICAO = "descricao";
    private static final String STATUS_PEDIDO = "statusPedido";

    public static boolean isPossuiCaracteresEspecias(String valor){
        boolean isPossuiCaracteresEspeciais = false;
        String padrao = "[@_!#$%^&*()<>?/|}{~:]";

        for (Character caractere : valor.toCharArray()) {
            Pattern pattern = Pattern.compile(padrao);
            if(pattern.matcher(String.valueOf(caractere)).find()){
                isPossuiCaracteresEspeciais = true;
                break;
            }
        }
        return isPossuiCaracteresEspeciais;
    }

    public static boolean isPossuiCaractereMaiusculas(String valor){
        boolean isPossuiCaractereMaiusculas = false;
        for(Character caractere : valor.toCharArray()){
            if(Character.isUpperCase(caractere)){
                isPossuiCaractereMaiusculas = true;
                break;
            }
        }
        return isPossuiCaractereMaiusculas;
    }

    public static boolean isPossuiCaractereMinusculas(String valor){
        boolean isPossuiCaractereMinusculas = false;
        for(Character caractere : valor.toCharArray()){
            if(Character.isLowerCase(caractere)){
                isPossuiCaractereMinusculas = true;
                break;
            }
        }
        return isPossuiCaractereMinusculas;
    }

    public static boolean isPossuiCaractereDigito(String valor){
        boolean isPossuiCaractereDigito = false;
        for(Character caractere : valor.toCharArray()){
            if(Character.isDigit(caractere)){
                isPossuiCaractereDigito = true;
                break;
            }
        }
        return isPossuiCaractereDigito;
    }

    public static NotificacaoDTO montarPedidoRealizado(Pedido pedido){
        Usuario usuario = pedido.getUsuario();

        ZonedDateTime dataPedido = toFusoPadraoSistema(pedido.getData());
        PedidoDTO pedidoDTO = PedidoDTO.builder()
                .descricao(usuario.getNome().concat(", seu pedido já foi encaminhado"))
                .codigoPedido(pedido.getId() != null ? pedido.getId().toString() : null)
                .dataPedido(FORMAT_DATA_HORA_MINUTO_SEGUNDO.format(dataPedido))
                .statusPedido(pedido.getPedidoStatus() != null ? pedido.getPedidoStatus().toString() : null)
                .valorTotalPedido(valorMonetarioBrasil(pedido.getPrecoTotal()))
                .itensPedidos(new ArrayList<>())
                .build();

        pedido.getItensPedidos().forEach(itemPedido -> {
            ItemPedidoDTO item = ItemPedidoDTO.builder()
                    .produto(itemPedido.getProduto().getNome())
                    .valorProduto(valorMonetarioBrasil(itemPedido.getValorAtualProduto()))
                    .quantidade(itemPedido.getQuantidade().toString())
                    .subtotal(valorMonetarioBrasil(itemPedido.getSubTotal()))
                    .build();
            pedidoDTO.getItensPedidos().add(item);
        });

        String nomeRemetente = "Loja Virtual";
        String titulo = "Pedido realizado com sucesso";
        String[] destinatarios = new String[]{usuario.getEmail()};
        EmailTemplateEnum emailTemplate = EmailTemplateEnum.PEDIDOS;

        Map<String, Object> dados = new HashMap<>();
        dados.put(DESCRICAO, pedidoDTO.getDescricao());
        dados.put("codigoPedido", pedidoDTO.getCodigoPedido());
        dados.put("dataPedido", pedidoDTO.getDataPedido());
        dados.put(STATUS_PEDIDO, pedidoDTO.getStatusPedido());
        dados.put("valorTotalPedido", pedidoDTO.getValorTotalPedido());
        dados.put("itensPedidos", pedidoDTO.getItensPedidos());

        return new NotificacaoDTO(nomeRemetente, titulo, destinatarios, emailTemplate, dados);
    }

    public static NotificacaoDTO montarPedidoFinalizado(Pedido pedido){
        Usuario usuario = pedido.getUsuario();

        NotificacaoDTO notificacaoDTO = montarPedidoRealizado(pedido);
        notificacaoDTO.setTitulo("Pedido finalizado");
        ZonedDateTime dataPedidoFinalizado = toFusoPadraoSistema(pedido.getDataFinalizado());
        notificacaoDTO.getDados().put("dataPedidoFinalizado", FORMAT_DATA_HORA_MINUTO_SEGUNDO.format(dataPedidoFinalizado));

        if(notificacaoDTO.getDados().containsKey(DESCRICAO)){
            notificacaoDTO.getDados().put(DESCRICAO, usuario.getNome().concat(", seu pedido já foi finalizado"));
        }

        if(notificacaoDTO.getDados().containsKey(STATUS_PEDIDO)){
            notificacaoDTO.getDados().put(STATUS_PEDIDO, pedido.getPedidoStatus().toString());
        }

        return notificacaoDTO;
    }

    public static NotificacaoDTO montarPedidoCancelado(Pedido pedido){
        Usuario usuario = pedido.getUsuario();

        NotificacaoDTO notificacaoDTO = montarPedidoRealizado(pedido);
        notificacaoDTO.setTitulo("Pedido cancelado");
        ZonedDateTime dataPedidoCancelado = toFusoPadraoSistema(pedido.getDataCancelado());
        notificacaoDTO.getDados().put("dataPedidoCancelado", FORMAT_DATA_HORA_MINUTO_SEGUNDO.format(dataPedidoCancelado));

        if(notificacaoDTO.getDados().containsKey(DESCRICAO)){
            notificacaoDTO.getDados().put(DESCRICAO, usuario.getNome().concat(", seu pedido já foi cancelado"));
        }

        if(notificacaoDTO.getDados().containsKey(STATUS_PEDIDO)){
            notificacaoDTO.getDados().put(STATUS_PEDIDO, pedido.getPedidoStatus().toString());
        }

        return notificacaoDTO;
    }
}
