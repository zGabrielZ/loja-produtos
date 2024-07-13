package br.com.gabrielferreira.produtos.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedidoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1026011307982030477L;

    private String produto;

    private String valorProduto;

    private String quantidade;

    private String subtotal;
}
