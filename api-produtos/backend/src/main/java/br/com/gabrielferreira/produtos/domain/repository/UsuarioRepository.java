package br.com.gabrielferreira.produtos.domain.repository;

import br.com.gabrielferreira.produtos.domain.model.Usuario;
import br.com.gabrielferreira.produtos.domain.repository.projection.UsuarioMinProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u.id as id, u.email as email FROM Usuario u " +
            "WHERE u.email = :email")
    Optional<UsuarioMinProjection> buscarPorEmail(@Param("email") String email);

    Page<Usuario> findAll(Specification<Usuario> specification, Pageable pageable);

    @Query("SELECT u FROM Usuario u " +
            "JOIN FETCH u.perfis p " +
            "WHERE u.id = :id")
    Optional<Usuario> buscarUsuarioPorId(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u " +
            "WHERE u.id = :id")
    boolean existsUsuarioPorId(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u " +
            "JOIN u.pedidos p " +
            "WHERE u.id = :id AND p.id = :idPedido")
    boolean existsUsuarioComPedidoPorId(@Param("id") Long id, @Param("idPedido") Long idPedido);
}
