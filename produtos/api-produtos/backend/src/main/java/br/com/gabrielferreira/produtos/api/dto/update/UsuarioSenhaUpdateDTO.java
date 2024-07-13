package br.com.gabrielferreira.produtos.api.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class UsuarioSenhaUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7534896478690802251L;

    @Schema(description = "Nova senha do usuário", example = "12345")
    @NotBlank
    @Size(min = 1, max = 255)
    private String novaSenha;

    @Schema(description = "Antiga senha do usuário", example = "123")
    @NotBlank
    @Size(min = 1, max = 255)
    private String antigaSenha;
}
