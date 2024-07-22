package br.com.gabrielferreira.notificacao.domain.repository;

import br.com.gabrielferreira.notificacao.domain.model.Notificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    Page<Notificacao> findAll(Specification<Notificacao> specification, Pageable pageable);
}
