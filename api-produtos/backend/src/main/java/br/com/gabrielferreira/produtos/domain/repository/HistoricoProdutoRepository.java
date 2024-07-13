package br.com.gabrielferreira.produtos.domain.repository;

import br.com.gabrielferreira.produtos.domain.model.HistoricoProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoricoProdutoRepository extends JpaRepository<HistoricoProduto, Long> {

    @Query("SELECT h FROM HistoricoProduto h " +
            "JOIN FETCH h.produto p " +
            "WHERE p.id = :idProduto")
    Page<HistoricoProduto> buscarHistoricosProdutosPorIdProduto(@Param("idProduto") Long idProduto, Pageable pageable);

    @Query("SELECT h FROM HistoricoProduto h " +
            "JOIN FETCH h.produto p " +
            "WHERE p.id = :idProduto AND h.id = :id")
    Optional<HistoricoProduto> buscarHistoricoProdutoPorId(@Param("idProduto") Long idProduto, @Param("id") Long id);
}
