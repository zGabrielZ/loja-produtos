package br.com.gabrielferreira.produtos.domain.exception.model;

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
public class ErroPadrao implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private ZonedDateTime dataAtual;

    private Integer status;

    private String titulo;

    private String mensagem;

    private String caminhoUrl;
}
