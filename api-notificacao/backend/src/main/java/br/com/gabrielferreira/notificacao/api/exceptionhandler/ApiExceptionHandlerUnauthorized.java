package br.com.gabrielferreira.notificacao.api.exceptionhandler;

import br.com.gabrielferreira.notificacao.api.mapper.ErroPadraoMapper;
import br.com.gabrielferreira.notificacao.domain.exception.model.ErroPadrao;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.IOException;
import java.time.ZonedDateTime;

import static br.com.gabrielferreira.notificacao.common.utils.DataUtils.toFusoPadraoSistema;

@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandlerUnauthorized implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    private final ErroPadraoMapper erroPadraoMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(httpStatus.value());

        ErroPadrao erroPadrao = erroPadraoMapper.toErroPadrao(toFusoPadraoSistema(ZonedDateTime.now()), httpStatus.value(), "Não autorizado", "Você precisa fazer login primeiro para executar esta função", request.getRequestURI());

        String json = objectMapper.writeValueAsString(erroPadrao);
        response.getWriter().write(json);
    }
}
