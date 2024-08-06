package br.com.gabrielferreira.notificacao.common.config.security.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
@Log4j2
public class TokenService {

    private final String chave;

    public TokenService(@Value("${jwt.secret}") String chave) {
        this.chave = chave;
    }

    public String getEmail(String token){
        return extrairTodoClaims(token).get("email").toString();
    }

    public String getPerfis(String token){
        return extrairTodoClaims(token).get("perfis").toString();
    }

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

    public Claims extrairTodoClaims(String token) {
        return Jwts.parser().verifyWith(getSecret()).build().parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecret(){
        return Keys.hmacShaKeyFor(chave.getBytes(StandardCharsets.UTF_8));
    }
}
