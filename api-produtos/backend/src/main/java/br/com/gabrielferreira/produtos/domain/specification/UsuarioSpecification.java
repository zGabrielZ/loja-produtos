package br.com.gabrielferreira.produtos.domain.specification;

import br.com.gabrielferreira.produtos.domain.model.Usuario;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecification {

    private UsuarioSpecification(){}

    public static Specification<Usuario> buscarPorNome(String nome){
        return (root, query, criteriaBuilder) -> {
            if(StringUtils.isNotBlank(nome)){
                return criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"
                );
            }
            return null;
        };
    }

    public static Specification<Usuario> buscarPorEmail(String email){
        return (root, query, criteriaBuilder) -> {
            if(StringUtils.isNotBlank(email)){
                return criteriaBuilder.equal(root.get("email"), email);
            }
            return null;
        };
    }
}
