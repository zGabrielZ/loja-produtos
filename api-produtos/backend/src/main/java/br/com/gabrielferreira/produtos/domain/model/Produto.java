package br.com.gabrielferreira.produtos.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static br.com.gabrielferreira.produtos.common.utils.DataUtils.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"historicoProdutos", "itensPedidos"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TB_PRODUTO")
public class Produto implements Serializable {

    @Serial
    private static final long serialVersionUID = 4400760583283403899L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "NOME", nullable = false, unique = true)
    private String nome;

    @Column(name = "PRECO", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
    private List<ItemPedido> itensPedidos = new ArrayList<>();

    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<HistoricoProduto> historicoProdutos = new ArrayList<>();

    @Column(name = "DATA_INCLUSAO", nullable = false)
    private ZonedDateTime dataInclusao;

    @Column(name = "DATA_ATUALIZACAO")
    private ZonedDateTime dataAtualizacao;

    @PrePersist
    public void prePersist(){
        dataInclusao = ZonedDateTime.now(UTC);
    }

    @PreUpdate
    public void preUpdate(){
        dataAtualizacao = ZonedDateTime.now(UTC);
    }

    public void adicionarHistoricoProduto(){
        HistoricoProduto historicoProduto = HistoricoProduto.builder()
                .nome(this.nome)
                .preco(this.preco)
                .produto(this)
                .build();

        if(this.historicoProdutos == null){
            this.historicoProdutos = new ArrayList<>();
        }

        this.historicoProdutos.add(historicoProduto);
    }
}
