package br.com.gabrielferreira.produtos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemPedidoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3669665560560096730L;

    @Schema(description = "Id do item pedido", example = "1")
    private Long id;

    @Schema(description = "Quantidade do item pedido", example = "2")
    private Integer quantidade;

    @Schema(description = "Valor atual do produto do item pedido", example = "2.00")
    private BigDecimal valorAtualProduto;

    @Schema(description = "Subtotal do item pedido", example = "4.00")
    private BigDecimal subTotal;

    @Schema(description = "Produto do item pedido")
    private ProdutoDTO produto;
}
