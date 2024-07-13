package br.com.gabrielferreira.produtos.api.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class PedidoCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6168950072508421375L;

    @Schema(description = "Itens pedidos do pedido")
    @Valid
    @NotNull
    @NotEmpty
    private List<ItemPedidoCreateDTO> itensPedidos = new ArrayList<>();
}
