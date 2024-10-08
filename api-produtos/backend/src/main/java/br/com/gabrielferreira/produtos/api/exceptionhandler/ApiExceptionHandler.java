package br.com.gabrielferreira.produtos.api.exceptionhandler;

import br.com.gabrielferreira.produtos.api.mapper.ErroPadraoMapper;
import br.com.gabrielferreira.produtos.domain.exception.*;
import br.com.gabrielferreira.produtos.domain.exception.model.ErroPadrao;
import br.com.gabrielferreira.produtos.domain.exception.model.ErroPadraoCampos;
import br.com.gabrielferreira.produtos.domain.exception.model.ErroPadraoFormulario;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.ZonedDateTime;
import java.util.List;

import static br.com.gabrielferreira.produtos.common.utils.DataUtils.*;

@ControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class ApiExceptionHandler {

    private final ErroPadraoMapper erroPadraoMapper;

    private final MessageSource messageSource;

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroPadrao> msgException(RegraDeNegocioException e, HttpServletRequest request){
        log.warn("msgException message : {}, requestUrl : {}", e.getMessage(), request.getRequestURI());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErroPadrao erroPadrao = erroPadraoMapper.toErroPadrao(toFusoPadraoSistema(ZonedDateTime.now()), httpStatus.value(), "Regra de negócio", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(erroPadrao);
    }

    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<ErroPadrao> naoEncontradoException(NaoEncontradoException e, HttpServletRequest request){
        log.warn("naoEncontradoException message : {}, id : {}, requestUrl : {}", e.getMessage(), e.getId(), request.getRequestURI());
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErroPadrao erroPadraoModel = erroPadraoMapper.toErroPadrao(toFusoPadraoSistema(ZonedDateTime.now()), httpStatus.value(), "Não encontrado", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(erroPadraoModel);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroPadrao> validacaoException(MethodArgumentNotValidException e, HttpServletRequest request){
        log.warn("validacaoException message : {}, requestUrl : {}", e.getMessage(), request.getRequestURI());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErroPadraoCampos erroPadraoCampos = erroPadraoMapper.toErroPadraoCampos(toFusoPadraoSistema(ZonedDateTime.now()), httpStatus.value(), "Erro validação de campos", "Ocorreu um erro de validação nos campos", request.getRequestURI());

        List<ErroPadraoFormulario> campos = e.getBindingResult().getFieldErrors().stream()
                .map(campo -> erroPadraoMapper.toErroPadraoFormulario(campo.getField(), messageSource.getMessage(campo, LocaleContextHolder.getLocale())))
                .toList();
        erroPadraoCampos.setCampos(campos);

        return ResponseEntity.status(httpStatus).body(erroPadraoCampos);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroPadrao> erroException(Exception e, HttpServletRequest request){
        if(e instanceof AccessDeniedException){
            return forbiddenException(new ForbiddenException("Você não tem permissão para realizar a requisição"), request);
        }

        log.error("erroException message : {}, requestUrl : {}", e.getMessage(), request.getRequestURI());
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErroPadrao erroPadrao = erroPadraoMapper.toErroPadrao(toFusoPadraoSistema(ZonedDateTime.now()), httpStatus.value(), "Erro inesperado", "Ocorreu um erro inesperado no sistema, tente mais tarde", request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(erroPadrao);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroPadrao> dataIntegrityViolationException(HttpServletRequest request){
        log.warn("dataIntegrityViolationException requestUrl : {}", request.getRequestURI());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErroPadrao erroPadrao = erroPadraoMapper.toErroPadrao(toFusoPadraoSistema(ZonedDateTime.now()), httpStatus.value(), "Violação de integridade", "Esta entidade possui relacionamento", request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(erroPadrao);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErroPadrao> unauthorizedException(UnauthorizedException e, HttpServletRequest request){
        log.error("unauthorizedException message : {}, requestUrl : {}", e.getMessage(), request.getRequestURI());
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ErroPadrao erroPadrao = erroPadraoMapper.toErroPadrao(toFusoPadraoSistema(ZonedDateTime.now()), httpStatus.value(), "Não autorizado", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(erroPadrao);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErroPadrao> forbiddenException(ForbiddenException e, HttpServletRequest request){
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ErroPadrao erroPadrao = erroPadraoMapper.toErroPadrao(toFusoPadraoSistema(ZonedDateTime.now()), httpStatus.value(), "Proibido", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(erroPadrao);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFound(Exception ex) {
        throw new MsgException(ex.getMessage());
    }
}
