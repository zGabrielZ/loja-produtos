package br.com.gabrielferreira.produtos.api.dto;

import br.com.gabrielferreira.produtos.domain.model.enums.PedidoStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6168950072508421375L;

    @Schema(description = "Id do pedido", example = "1")
    private Long id;

    @Schema(description = "Data do pedido criado", example = "2024-02-11T16:49:23.177681-03:00")
    private ZonedDateTime data;

    @Schema(description = "Data do pedido finalizado", example = "2024-02-11T16:49:23.177681-03:00")
    private ZonedDateTime dataFinalizado;

    @Schema(description = "Data do pedido cancelado", example = "2024-02-11T16:49:23.177681-03:00")
    private ZonedDateTime dataCancelado;

    @Schema(description = "Status do pedido", example = "ABERTO")
    private PedidoStatusEnum pedidoStatus;

    @Schema(description = "Data inclusão do pedido", example = "2024-02-11T16:49:23.177681-03:00")
    private ZonedDateTime dataInclusao;

    @Schema(description = "Data atualização do pedido", example = "2024-02-11T16:49:23.177681-03:00")
    private ZonedDateTime dataAtualizacao;

    @Schema(description = "Preço total do pedido", example = "20.00")
    private BigDecimal precoTotal;

    @Schema(description = "Itens pedidos do pedido")
    private List<ItemPedidoDTO> itensPedidos = new ArrayList<>();
}
