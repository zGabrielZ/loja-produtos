package br.com.gabrielferreira.produtos.domain.exception;

import java.io.Serial;

public class MsgException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6241970198232568585L;

    public MsgException(String msg){
        super(msg);
    }
}
