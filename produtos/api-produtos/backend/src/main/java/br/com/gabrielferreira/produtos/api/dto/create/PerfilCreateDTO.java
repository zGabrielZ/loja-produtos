package br.com.gabrielferreira.produtos.api.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
public class PerfilCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1992239811546407642L;

    @Schema(description = "Id do perfil", example = "1")
    @NotNull
    private Long id;
}
