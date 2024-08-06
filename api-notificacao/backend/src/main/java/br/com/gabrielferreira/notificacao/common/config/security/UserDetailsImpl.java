package br.com.gabrielferreira.notificacao.common.config.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDetailsImpl implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = 7081591722637398890L;

    private String email;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl construirUserDetails(String email, String perfis){
        List<SimpleGrantedAuthority> authorityList = Arrays.stream(perfis.split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        return UserDetailsImpl.builder()
                .email(email)
                .authorities(authorityList)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
