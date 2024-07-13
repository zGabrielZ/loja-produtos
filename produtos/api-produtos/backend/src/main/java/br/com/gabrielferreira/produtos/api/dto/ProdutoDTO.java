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
public class ProdutoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -178664660432861224L;

    @Schema(description = "Id do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto", example = "Laranja")
    private String nome;

    @Schema(description = "Preço do produto", example = "4.00")
    private BigDecimal preco;

    @Schema(description = "Data inclusão do produto", example = "2024-02-11T16:49:23.177681-03:00")
    private ZonedDateTime dataInclusao;

    @Schema(description = "Data atualização do produto", example = "2024-02-11T16:49:23.177681-03:00")
    private ZonedDateTime dataAtualizacao;
}
