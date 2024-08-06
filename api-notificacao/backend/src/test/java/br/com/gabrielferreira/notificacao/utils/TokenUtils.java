package br.com.gabrielferreira.notificacao.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static br.com.gabrielferreira.notificacao.common.utils.DataUtils.UTC;

@Component
public class TokenUtils {

    private final String chave;

    public TokenUtils(@Value("${jwt.secret}") String chave) {
        this.chave = chave;
    }

    public String gerarToken(List<String> perfis){
        SecretKey secretKey = Keys.hmacShaKeyFor(chave.getBytes(StandardCharsets.UTF_8));
        ZonedDateTime dataAtual = ZonedDateTime.now(UTC);
        ZonedDateTime dataExpiracao = dataAtual.plus(Duration.ofMillis(3600000L));

        String perfil = String.join(",", perfis);
        return Jwts.builder()
                .issuer("Teste")
                .subject("Teste")
                .issuedAt(Date.from(dataAtual.toInstant()))
                .expiration(Date.from(dataExpiracao.toInstant()))
                .claim("email", "teste@email.com.br")
                .claim("perfis", perfil)
                .signWith(secretKey)
                .compact();
    }
}
