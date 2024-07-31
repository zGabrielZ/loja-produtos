package br.com.gabrielferreira.produtos.domain.exception;

import java.io.Serial;

public class ForbiddenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6241970198232568585L;

    public ForbiddenException(String msg){
        super(msg);
    }
}
