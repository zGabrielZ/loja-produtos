package br.com.gabrielferreira.produtos.domain.repository;

import br.com.gabrielferreira.produtos.domain.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p " +
            "JOIN FETCH p.usuario u " +
            "LEFT JOIN FETCH p.itensPedidos ip " +
            "LEFT JOIN FETCH ip.produto pro " +
            "WHERE u.id = :idUsuario AND p.id = :idPedido")
    Optional<Pedido> buscarPedido(@Param("idUsuario") Long idUsuario, @Param("idPedido") Long idPedido);

    @Query("SELECT p FROM Pedido p " +
            "JOIN FETCH p.usuario u " +
            "LEFT JOIN FETCH p.itensPedidos ip " +
            "LEFT JOIN FETCH ip.produto pro " +
            "WHERE u.id = :idUsuario")
    Page<Pedido> buscarPedidos(@Param("idUsuario") Long idUsuario, Pageable pageable);
}
