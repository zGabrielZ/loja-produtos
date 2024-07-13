package br.com.gabrielferreira.produtos.tests;

import br.com.gabrielferreira.produtos.api.dto.ItemPedidoDTO;
import br.com.gabrielferreira.produtos.api.dto.PedidoDTO;
import br.com.gabrielferreira.produtos.api.dto.ProdutoDTO;
import br.com.gabrielferreira.produtos.api.dto.create.ItemPedidoCreateDTO;
import br.com.gabrielferreira.produtos.api.dto.create.PedidoCreateDTO;
import br.com.gabrielferreira.produtos.domain.model.ItemPedido;
import br.com.gabrielferreira.produtos.domain.model.Pedido;
import br.com.gabrielferreira.produtos.domain.model.Produto;
import br.com.gabrielferreira.produtos.domain.model.enums.PedidoStatusEnum;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoFactory {

    private PedidoFactory(){}

    public static PedidoCreateDTO criarPedido(){
        List<ItemPedidoCreateDTO> itens = new ArrayList<>();
        itens.add(new ItemPedidoCreateDTO(2, 1L));
        itens.add(new ItemPedidoCreateDTO(3, 1L));
        itens.add(new ItemPedidoCreateDTO(5, 2L));

        return PedidoCreateDTO.builder()
                .itensPedidos(itens)
                .build();
    }

    public static Pedido criarPedidoModel(){
        PedidoCreateDTO pedidoCreateDTO = criarPedido();
        List<ItemPedido> itens = new ArrayList<>();
        pedidoCreateDTO.getItensPedidos().forEach(item -> {
            ItemPedido itemPedido = ItemPedido.builder()
                    .quantidade(item.getQuantidade())
                    .produto(Produto.builder().id(item.getIdProduto()).build())
                    .build();

            itens.add(itemPedido);
        });

        return Pedido.builder()
                .itensPedidos(itens)
                .build();
    }

    public static Pedido criarPedidoModelAposSalvar(){
        Pedido pedido = criarPedidoModel();
        pedido.setId(1L);
        pedido.setData(ZonedDateTime.now());
        pedido.setPedidoStatus(PedidoStatusEnum.ABERTO);
        pedido.setDataInclusao(ZonedDateTime.now());

        pedido.getItensPedidos().get(0).setId(1L);
        pedido.getItensPedidos().get(0).setValorAtualProduto(BigDecimal.ONE);

        pedido.getItensPedidos().get(1).setId(2L);
        pedido.getItensPedidos().get(1).setValorAtualProduto(BigDecimal.ONE);

        pedido.getItensPedidos().get(2).setId(3L);
        pedido.getItensPedidos().get(2).setValorAtualProduto(BigDecimal.ONE);
        return pedido;
    }

    public static PedidoDTO criarPedidoDtoAposSalvar(){
        Pedido pedido = criarPedidoModelAposSalvar();
        List<ItemPedidoDTO> itemPedidoDTOS = new ArrayList<>();
        pedido.getItensPedidos().forEach(item -> {
            ItemPedidoDTO itemPedidoDTO = ItemPedidoDTO.builder()
                    .id(item.getId())
                    .quantidade(item.getQuantidade())
                    .valorAtualProduto(item.getValorAtualProduto())
                    .subTotal(item.getSubTotal())
                    .produto(ProdutoDTO.builder().id(item.getProduto().getId()).build())
                    .build();

            itemPedidoDTOS.add(itemPedidoDTO);
        });

        return PedidoDTO.builder()
                .id(pedido.getId())
                .data(pedido.getData())
                .pedidoStatus(pedido.getPedidoStatus())
                .dataInclusao(pedido.getDataInclusao())
                .precoTotal(pedido.getPrecoTotal())
                .itensPedidos(itemPedidoDTOS)
                .build();
    }
}
