package br.com.gabrielferreira.produtos.domain.service.impl;

import br.com.gabrielferreira.produtos.common.config.security.UserDetailsImpl;
import br.com.gabrielferreira.produtos.domain.service.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;

import static br.com.gabrielferreira.produtos.common.utils.DataUtils.*;

@Service
@Log4j2
public class TokenServiceImpl implements TokenService {

    private final String chave;

    private final String expiracao;

    public TokenServiceImpl(@Value("${jwt.secret}") String chave, @Value("${jwt.expiration}") String expiracao) {
        this.chave = chave;
        this.expiracao = expiracao;
    }

    @Override
    public String gerarToken(UserDetailsImpl userDetailsImpl) {
        SecretKey secretKey = getSecret();
        ZonedDateTime dataAtual = ZonedDateTime.now(UTC);
        ZonedDateTime dataExpiracao = dataAtual.plus(Duration.ofMillis(Long.parseLong(expiracao)));

        log.info("Data atual do token gerado em UTC {}", dataAtual);
        log.info("Data expiração do token gerado em UTC {}", dataExpiracao);

        return Jwts.builder()
                .issuer("API Produtos")
                .subject("API de Produtos")
                .issuedAt(Date.from(dataAtual.toInstant()))
                .expiration(Date.from(dataExpiracao.toInstant()))
                .claim("idUsuario", userDetailsImpl.getId())
                .signWith(secretKey)
                .compact();
    }

    @Override
    public boolean isTokenValido(String token) {
        try {
            extrairTodoClaims(token);
            return true;
        } catch (SignatureException e){
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e){
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e){
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e){
            log.error("JWT token is unssupported: {}", e.getMessage());
        } catch (IllegalArgumentException e){
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    @Override
    public Claims extrairTodoClaims(String token) {
        return Jwts.parser().verifyWith(getSecret()).build().parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecret(){
        return Keys.hmacShaKeyFor(chave.getBytes(StandardCharsets.UTF_8));
    }
}
