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
public class TokenDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7039661903095606395L;

    @Schema(description = "Tipo do token", example = "Bearer")
    private String tipo;

    @Schema(description = "Token gerado", example = "123123123")
    private String token;
}
