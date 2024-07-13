package br.com.gabrielferreira.produtos.api.dto.update;

import br.com.gabrielferreira.produtos.api.dto.create.ProdutoCreateDTO;
import lombok.EqualsAndHashCode;
import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
public class ProdutoUpdateDTO extends ProdutoCreateDTO {

    @Serial
    private static final long serialVersionUID = 5484393252615917081L;
}
