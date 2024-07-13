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
public class ItemPedidoResumidoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7139919889912820004L;

    @Schema(description = "Id do item pedido", example = "1")
    private Long id;

    @Schema(description = "Quantidade do item pedido", example = "5")
    private Integer quantidade;

    @Schema(description = "Valor atual do produto que foi inserido no item pedido", example = "5.00")
    private BigDecimal valorAtualProduto;

    @Schema(description = "Subtotal do item pedido", example = "25.00")
    private BigDecimal subTotal;

    @Schema(description = "Produto do item pedido")
    private ProdutoDTO produto;

    @Schema(description = "Usuário do item pedido")
    private UsuarioResumidoDTO usuario;
}
