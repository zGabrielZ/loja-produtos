package br.com.gabrielferreira.produtos.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1026011307982030477L;

    private String descricao;

    private String codigoPedido;

    private String dataPedido;

    private String dataPedidoFinalizado;

    private String dataPedidoCancelado;

    private String statusPedido;

    private String valorTotalPedido;

    private List<ItemPedidoDTO> itensPedidos = new ArrayList<>();
}
