package br.com.gabrielferreira.produtos.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static br.com.gabrielferreira.produtos.common.utils.DataUtils.UTC;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TB_HISTORICO_PRODUTO")
public class HistoricoProduto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1560834883495261029L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "NOME", nullable = false, unique = true)
    private String nome;

    @Column(name = "PRECO", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRODUTO", nullable = false)
    private Produto produto;

    @Column(name = "DATA_INCLUSAO", nullable = false)
    private ZonedDateTime dataInclusao;

    @PrePersist
    public void prePersist(){
        dataInclusao = ZonedDateTime.now(UTC);
    }
}
