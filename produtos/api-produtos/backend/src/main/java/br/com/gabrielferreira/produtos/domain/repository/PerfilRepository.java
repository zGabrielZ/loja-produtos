package br.com.gabrielferreira.produtos.domain.repository;

import br.com.gabrielferreira.produtos.domain.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    @Query("SELECT p FROM Perfil p " +
            "ORDER BY p.descricao ASC")
    List<Perfil> buscarPerfis();

    @Query("SELECT p FROM Perfil p " +
            "JOIN FETCH p.usuarios u " +
            "WHERE u.id = :idUsuario AND p.id = :id")
    Optional<Perfil> buscarPerfilPorId(@Param("idUsuario") Long idUsuario, @Param("id") Long id);

    @Query("SELECT p FROM Perfil p " +
            "JOIN FETCH p.usuarios u " +
            "WHERE u.id = :idUsuario")
    List<Perfil> buscarPerfisPorIdUsuario(@Param("idUsuario") Long idUsuario);

}
