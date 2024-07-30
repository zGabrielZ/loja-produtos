package br.com.gabrielferreira.produtos.domain.exception;

import java.io.Serial;

public class UnauthorizedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6241970198232568585L;

    public UnauthorizedException(String msg){
        super(msg);
    }
}
