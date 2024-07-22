package br.com.gabrielferreira.notificacao.api.exceptionhandler;

import br.com.gabrielferreira.notificacao.api.mapper.ErroPadraoMapper;
import br.com.gabrielferreira.notificacao.domain.exception.MsgException;
import br.com.gabrielferreira.notificacao.domain.exception.model.ErroPadrao;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.ZonedDateTime;

import static br.com.gabrielferreira.notificacao.common.utils.DataUtils.*;

@ControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class ApiExceptionHandler {

    private final ErroPadraoMapper erroPadraoMapper;

    @ExceptionHandler(MsgException.class)
    public ResponseEntity<ErroPadrao> msgException(MsgException e, HttpServletRequest request){
        log.warn("msgException message : {}, requestUrl : {}", e.getMessage(), request.getRequestURI());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErroPadrao erroPadrao = erroPadraoMapper.toErroPadrao(toFusoPadraoSistema(ZonedDateTime.now()), httpStatus.value(), "Erro ao realizar requisição", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(erroPadrao);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroPadrao> erroException(Exception e, HttpServletRequest request){
        log.error("erroException message : {}, requestUrl : {}", e.getMessage(), request.getRequestURI());
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErroPadrao erroPadrao = erroPadraoMapper.toErroPadrao(toFusoPadraoSistema(ZonedDateTime.now()), httpStatus.value(), "Erro inesperado", "Ocorreu um erro inesperado no sistema, tente mais tarde", request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(erroPadrao);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFound(Exception ex) {
        throw new RuntimeException(ex.getMessage());
    }
}
