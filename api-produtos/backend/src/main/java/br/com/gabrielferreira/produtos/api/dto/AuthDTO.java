package br.com.gabrielferreira.produtos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
public class AuthDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3467335872529452895L;

    @Schema(description = "E-mail do usuário", example = "teste@email.com")
    @NotBlank
    @Email
    @Size(min = 1, max = 255)
    private String email;

    @Schema(description = "Senha do usuário", example = "123")
    @NotBlank
    @Size(min = 1, max = 255)
    private String senha;
}
