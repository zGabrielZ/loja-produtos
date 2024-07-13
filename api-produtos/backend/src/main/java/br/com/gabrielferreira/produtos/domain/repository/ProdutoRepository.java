package br.com.gabrielferreira.produtos.domain.repository;

import br.com.gabrielferreira.produtos.domain.model.Produto;
import br.com.gabrielferreira.produtos.domain.repository.projection.ProdutoMinProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p.id as id, p.nome as nome FROM Produto p " +
            "WHERE p.nome = :nome")
    Optional<ProdutoMinProjection> buscarPorNome(@Param("nome") String nome);

    Page<Produto> findAll(Specification<Produto> specification, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Produto p " +
            "WHERE p.id = :id")
    boolean existsProdutoPorId(@Param("id") Long id);
}
