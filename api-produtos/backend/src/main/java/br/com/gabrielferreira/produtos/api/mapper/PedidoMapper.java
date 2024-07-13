package br.com.gabrielferreira.produtos.api.mapper;

import br.com.gabrielferreira.produtos.api.dto.PedidoDTO;
import br.com.gabrielferreira.produtos.api.dto.PedidoResumidoDTO;
import br.com.gabrielferreira.produtos.api.dto.create.PedidoCreateDTO;
import br.com.gabrielferreira.produtos.domain.model.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = {FormatMapper.class, ItemPedidoMapper.class, UsuarioMapper.class})
public interface PedidoMapper {

    Pedido toPedido(PedidoCreateDTO pedidoCreateDTO);

    @Mapping(target = "data", qualifiedByName = "formatData")
    @Mapping(target = "dataFinalizado", qualifiedByName = "formatData")
    @Mapping(target = "dataCancelado", qualifiedByName = "formatData")
    @Mapping(target = "dataInclusao", qualifiedByName = "formatData")
    @Mapping(target = "dataAtualizacao", qualifiedByName = "formatData")
    PedidoDTO toPedidoDto(Pedido pedido);

    @Mapping(target = "data", qualifiedByName = "formatData")
    @Mapping(target = "dataFinalizado", qualifiedByName = "formatData")
    @Mapping(target = "dataCancelado", qualifiedByName = "formatData")
    @Mapping(target = "dataInclusao", qualifiedByName = "formatData")
    @Mapping(target = "dataAtualizacao", qualifiedByName = "formatData")
    PedidoResumidoDTO toPedidoResumidoDto(Pedido pedido);

    default Page<PedidoResumidoDTO> toPedidoResumidoDtos(Page<Pedido> pedidos){
        return pedidos.map(this::toPedidoResumidoDto);
    }
}
