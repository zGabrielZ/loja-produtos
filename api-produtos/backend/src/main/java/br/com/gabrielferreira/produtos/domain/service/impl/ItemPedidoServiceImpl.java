package br.com.gabrielferreira.produtos.domain.service.impl;

import br.com.gabrielferreira.produtos.common.config.security.UserDetailsImpl;
import br.com.gabrielferreira.produtos.common.config.security.service.UserDetailsAutenticacaoService;
import br.com.gabrielferreira.produtos.domain.exception.NaoEncontradoException;
import br.com.gabrielferreira.produtos.domain.exception.RegraDeNegocioException;
import br.com.gabrielferreira.produtos.domain.model.ItemPedido;
import br.com.gabrielferreira.produtos.domain.repository.ItemPedidoRepository;
import br.com.gabrielferreira.produtos.domain.service.ItemPedidoService;
import br.com.gabrielferreira.produtos.domain.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemPedidoServiceImpl implements ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;

    private final UsuarioService usuarioService;

    private final UserDetailsAutenticacaoService userDetailsAutenticacaoService;

    @Override
    public ItemPedido buscarItemPedidoPorId(Long idUsuario, Long idPedido, Long idItemPedido) {
        validarUsuarioComPedidoExistente(idUsuario, idPedido);
        validarUsuarioAutenticadoRealizacaoItemPedido(idUsuario, "Para realizar a busca do item do pedido deve ser o próprio usuário ou admin ou funcionário");
        return itemPedidoRepository.buscarItemPedidoPorId(idUsuario, idPedido, idItemPedido)
                .orElseThrow(() -> new NaoEncontradoException("Item pedido não encontrado", idUsuario, idPedido, idItemPedido));
    }

    @Override
    public Page<ItemPedido> buscarItensPedidosPaginados(Long idUsuario, Long idPedido, Pageable pageable) {
        validarUsuarioComPedidoExistente(idUsuario, idPedido);
        validarUsuarioAutenticadoRealizacaoItemPedido(idUsuario, "Para realizar a busca dos itens dos pedidos deve ser o próprio usuário ou admin ou funcionário");
        return itemPedidoRepository.buscarItemPedidos(idUsuario, idPedido, pageable);
    }

    private void validarUsuarioComPedidoExistente(Long idUsuario, Long idPedido){
        if(usuarioService.naoExisteUsuarioComPedido(idUsuario, idPedido)){
            throw new NaoEncontradoException("Usuário com este pedido não encontrado", idUsuario, idPedido);
        }
    }

    private void validarUsuarioAutenticadoRealizacaoItemPedido(Long idUsuario, String mensagem){
        UserDetailsImpl userDetailsAutenticado = userDetailsAutenticacaoService.buscarUsuarioAutenticado();
        boolean isPermissaoRealizarItemPedido = (userDetailsAutenticado.isAdmin() && !userDetailsAutenticado.getId().equals(idUsuario))
                || (userDetailsAutenticado.isFuncionario() && !userDetailsAutenticado.getId().equals(idUsuario))
                || userDetailsAutenticado.getId().equals(idUsuario);
        if(!isPermissaoRealizarItemPedido){
            throw new RegraDeNegocioException(mensagem);
        }
    }
}
