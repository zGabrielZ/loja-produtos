package br.com.gabrielferreira.produtos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class PerfilDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2618177754604831375L;

    @Schema(description = "Id do perfil", example = "1")
    private Long id;

    @Schema(description = "Descrição do perfil", example = "Cliente")
    private String descricao;

    @Schema(description = "Autoriedade do perfil", example = "ROLE_CLIENTE")
    private String autoriedade;
}
