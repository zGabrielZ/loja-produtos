package br.com.gabrielferreira.produtos.commons.dto;

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
public class ErroDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private ZonedDateTime dataAtual;
    
    private String descricao;
}
