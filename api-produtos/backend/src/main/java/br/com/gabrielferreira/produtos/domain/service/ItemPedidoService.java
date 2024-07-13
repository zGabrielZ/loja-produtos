package br.com.gabrielferreira.produtos.domain.service;

import br.com.gabrielferreira.produtos.domain.model.ItemPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemPedidoService {

    ItemPedido buscarItemPedidoPorId(Long idUsuario, Long idPedido, Long idItemPedido);

    Page<ItemPedido> buscarItensPedidosPaginados(Long idUsuario, Long idPedido, Pageable pageable);
}
