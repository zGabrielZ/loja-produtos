package br.com.gabrielferreira.notificacao.domain.specification;

import br.com.gabrielferreira.notificacao.domain.model.Notificacao;
import br.com.gabrielferreira.notificacao.domain.model.enums.NotificacaoStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class NotificacaoSpecification {

    private NotificacaoSpecification(){}

    public static Specification<Notificacao> buscarPorTitulo(String titulo){
        return (root, query, criteriaBuilder) -> {
            if(StringUtils.isNotBlank(titulo)){
                return criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%"
                );
            }
            return null;
        };
    }

    public static Specification<Notificacao> buscarPorStatus(NotificacaoStatusEnum status){
        return (root, query, criteriaBuilder) -> {
            if(status != null){
                return criteriaBuilder.equal(root.get("status"), status);
            }
            return null;
        };
    }
}
