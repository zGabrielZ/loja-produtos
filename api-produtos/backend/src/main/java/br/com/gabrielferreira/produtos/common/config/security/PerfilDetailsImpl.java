package br.com.gabrielferreira.produtos.common.config.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PerfilDetailsImpl implements Serializable, GrantedAuthority {

    @Serial
    private static final long serialVersionUID = -95728230168318715L;

    @EqualsAndHashCode.Include
    private Long id;

    private String descricao;

    private String autoriedade;

    @Override
    public String getAuthority() {
        return this.autoriedade;
    }
}
