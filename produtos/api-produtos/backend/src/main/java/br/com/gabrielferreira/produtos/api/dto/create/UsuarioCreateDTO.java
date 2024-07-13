package br.com.gabrielferreira.produtos.api.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "senha")
public class UsuarioCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7480897515495327124L;

    @Schema(description = "Nome do usuário", example = "Gabriel Ferreira")
    @NotBlank
    @Size(min = 1, max = 255)
    private String nome;

    @Schema(description = "E-mail do usuário", example = "teste@email.com")
    @NotBlank
    @Email
    @Size(min = 1, max = 255)
    private String email;

    @Schema(description = "Senha do usuário", example = "1243")
    @NotBlank
    @Size(min = 1, max = 255)
    private String senha;

    @Schema(description = "Perfis do usuário")
    @Valid
    @NotNull
    @NotEmpty
    private List<PerfilCreateDTO> perfis = new ArrayList<>();
}
