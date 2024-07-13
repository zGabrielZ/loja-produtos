package br.com.gabrielferreira.notificacao.domain.model;

import br.com.gabrielferreira.notificacao.domain.model.enums.NotificacaoStatusEnum;
import br.com.gabrielferreira.produtos.commons.enums.EmailTemplateEnum;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"destinatarios"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TB_NOTIFICACAO")
public class Notificacao implements Serializable {

    @Serial
    private static final long serialVersionUID = -3661209859042764780L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "NOME_REMETENTE")
    private String nomeRemetente;

    @Column(name = "TITULO")
    private String titulo;

    @OneToMany(mappedBy = "notificacao", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<UsuarioNotificacao> destinatarios = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EmailTemplateEnum emailTemplate;

    @Enumerated(EnumType.STRING)
    private NotificacaoStatusEnum status;

    @Column(name = "DATA_ATUAL_ERRO")
    private ZonedDateTime dataAtualErro;

    @Column(name = "DESCRICAO_ERRO")
    private String descricaoErro;
}
