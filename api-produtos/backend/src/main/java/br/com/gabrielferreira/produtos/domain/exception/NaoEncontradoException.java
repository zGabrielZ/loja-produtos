package br.com.gabrielferreira.produtos.domain.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class NaoEncontradoException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3163359517631172271L;

    private Long[] id;

    public NaoEncontradoException(String msg){
        super(msg);
    }

    public NaoEncontradoException(String msg, Long... id){
        this(msg);
        this.id = id;
    }
}
