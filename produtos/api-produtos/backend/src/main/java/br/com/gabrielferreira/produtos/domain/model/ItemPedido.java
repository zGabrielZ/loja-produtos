package br.com.gabrielferreira.produtos.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"produto", "pedido"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TB_ITEM_PEDIDO")
public class ItemPedido implements Serializable {

    @Serial
    private static final long serialVersionUID = 8489948922049610224L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "QUANTIDADE", nullable = false)
    private Integer quantidade;

    @Column(name = "VALOR_ATUAL_PRODUTO", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorAtualProduto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRODUTO", nullable = false)
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PEDIDO", nullable = false)
    private Pedido pedido;

    public BigDecimal getSubTotal(){
        return BigDecimal.valueOf(quantidade).multiply(valorAtualProduto);
    }
}
