package br.com.gabrielferreira.produtos.domain.repository;

import br.com.gabrielferreira.produtos.domain.model.ItemPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    @Query("SELECT ip FROM ItemPedido ip " +
            "JOIN FETCH ip.pedido p " +
            "JOIN FETCH p.usuario u " +
            "JOIN FETCH ip.produto prod " +
            "WHERE u.id = :idUsuario AND p.id = :idPedido " +
            "AND ip.id = :idItemPedido")
    Optional<ItemPedido> buscarItemPedidoPorId(@Param("idUsuario") Long idUsuario, @Param("idPedido") Long idPedido, @Param("idItemPedido") Long idItemPedido);

    @Query("SELECT ip FROM ItemPedido ip " +
            "JOIN FETCH ip.pedido p " +
            "JOIN FETCH p.usuario u " +
            "JOIN FETCH ip.produto prod " +
            "WHERE u.id = :idUsuario AND p.id = :idPedido ")
    Page<ItemPedido> buscarItemPedidos(@Param("idUsuario") Long idUsuario, @Param("idPedido") Long idPedido, Pageable pageable);
}
