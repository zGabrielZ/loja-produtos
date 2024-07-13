package br.com.gabrielferreira.produtos.domain.service;

import br.com.gabrielferreira.produtos.domain.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoService {

    Pedido salvarPedido(Long idUsuario, Pedido pedido);

    Pedido buscarPedidoPorId(Long idUsuario, Long idPedido);

    Pedido finalizarPedidoPorId(Long idUsuario, Long idPedido);

    Pedido cancelarPedidoPorId(Long idUsuario, Long idPedido);

    Page<Pedido> buscarPedidosPaginados(Long idUsuario, Pageable pageable);

    Pedido salvarPedidoEnviarNotificacao(Long idUsuario, Pedido pedido);

    void finalizarPedidoPorIdEnviarNotificacao(Long idUsuario, Long idPedido);

    void cancelarPedidoPorIdEnviarNotificacao(Long idUsuario, Long idPedido);
}
