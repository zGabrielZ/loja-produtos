package br.com.gabrielferreira.produtos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioResumidoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2867513301215529004L;

    @Schema(description = "Id do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome do usuário", example = "Gabriel Ferreira")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "teste@email.com.br")
    private String email;

    @Schema(description = "Data inclusão do usuário", example = "2024-02-11T16:49:23.177681-03:00")
    private ZonedDateTime dataInclusao;

    @Schema(description = "Data atualização do usuário", example = "2024-02-11T16:49:23.177681-03:00")
    private ZonedDateTime dataAtualizacao;
}
