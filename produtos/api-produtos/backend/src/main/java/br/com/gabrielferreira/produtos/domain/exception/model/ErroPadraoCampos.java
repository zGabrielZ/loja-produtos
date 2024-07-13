package br.com.gabrielferreira.produtos.domain.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ErroPadraoCampos extends ErroPadrao {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<ErroPadraoFormulario> campos = new ArrayList<>();
}
