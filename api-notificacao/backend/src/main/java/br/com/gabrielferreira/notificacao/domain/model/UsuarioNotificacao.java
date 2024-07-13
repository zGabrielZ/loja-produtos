package br.com.gabrielferreira.notificacao.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"notificacao"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TB_USUARIO_NOTIFICACAO")
public class UsuarioNotificacao implements Serializable {

    @Serial
    private static final long serialVersionUID = 7126744025130513663L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "EMAIL")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_NOTIFICACAO", nullable = false)
    private Notificacao notificacao;
}
