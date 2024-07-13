package br.com.gabrielferreira.produtos.api.dto.update;

import br.com.gabrielferreira.produtos.api.dto.create.PerfilCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7534896478690802251L;

    @Schema(description = "Nome do usuário", example = "Gabriel Ferreira")
    @NotBlank
    @Size(min = 1, max = 255)
    private String nome;

    @Schema(description = "Perfis do usuário")
    @Valid
    @NotEmpty
    @NotNull
    private List<PerfilCreateDTO> perfis = new ArrayList<>();
}
