package br.com.gabrielferreira.produtos.api.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemPedidoCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3669665560560096730L;

    @Schema(description = "Quantidade do item pedido", example = "3")
    @NotNull
    @Positive
    private Integer quantidade;

    @Schema(description = "Id do produto do item pedido", example = "1")
    @NotNull
    private Long idProduto;
}
