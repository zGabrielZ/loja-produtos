package br.com.gabrielferreira.produtos.domain.exception.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErroPadraoFormulario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String campo;

    private String mensagem;
}

