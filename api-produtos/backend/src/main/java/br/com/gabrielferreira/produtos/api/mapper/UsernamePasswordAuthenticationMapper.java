package br.com.gabrielferreira.produtos.api.mapper;

import br.com.gabrielferreira.produtos.api.dto.AuthDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthenticationMapper {

    public UsernamePasswordAuthenticationToken toUsernamePasswordAuthenticationToken(AuthDTO authDTO){
        return new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getSenha());
    }
}
