package br.com.gabrielferreira.produtos.api.mapper;

import br.com.gabrielferreira.produtos.api.dto.ItemPedidoDTO;
import br.com.gabrielferreira.produtos.api.dto.ItemPedidoResumidoDTO;
import br.com.gabrielferreira.produtos.api.dto.create.ItemPedidoCreateDTO;
import br.com.gabrielferreira.produtos.domain.model.ItemPedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProdutoMapper.class})
public interface ItemPedidoMapper {

    @Mapping(target = "produto.id", source = "idProduto")
    ItemPedido toItemPedido(ItemPedidoCreateDTO itemPedidoCreateDTO);

    default List<ItemPedido> toItensPedidos(List<ItemPedidoCreateDTO> itemPedidoCreateDTOS){
        return itemPedidoCreateDTOS.stream().map(this::toItemPedido).toList();
    }

    ItemPedidoDTO toItemPedidoDto(ItemPedido itemPedido);

    default List<ItemPedidoDTO> toItensPedidosDtos(List<ItemPedido> itemPedidos){
        return itemPedidos.stream().map(this::toItemPedidoDto).toList();
    }
    @Mapping(target = "usuario.id", source = "pedido.usuario.id")
    @Mapping(target = "usuario.nome", source = "pedido.usuario.nome")
    @Mapping(target = "usuario.email", source = "pedido.usuario.email")
    ItemPedidoResumidoDTO toItemPedidoResumidoDto(ItemPedido itemPedido);

    default Page<ItemPedidoResumidoDTO> toItemPedidoResumidoDtos(Page<ItemPedido> itemPedidos){
        return itemPedidos.map(this::toItemPedidoResumidoDto);
    }
}
