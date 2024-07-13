package br.com.gabrielferreira.produtos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoricoProdutoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5233762748354800626L;

    @Schema(description = "Id do histórico do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do histórico do produto", example = "Abacaxi")
    private String nome;

    @Schema(description = "Preço do histórico do produto", example = "3.40")
    private BigDecimal preco;

    @Schema(description = "Data inclusão do histórico do produto", example = "2024-02-11T16:49:23.177681-03:00")
    private ZonedDateTime dataInclusao;
}
