package br.com.gabrielferreira.produtos.api.controller;

import br.com.gabrielferreira.produtos.api.dto.ItemPedidoResumidoDTO;
import br.com.gabrielferreira.produtos.api.mapper.ItemPedidoMapper;
import br.com.gabrielferreira.produtos.domain.model.ItemPedido;
import br.com.gabrielferreira.produtos.domain.service.ItemPedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Item Pedido Controller", description = "Endpoints para realizar requisições de itens pedidos")
@RestController
@RequestMapping("/usuarios/{idUsuario}/pedidos/{idPedido}/itens")
@RequiredArgsConstructor
@Log4j2
public class ItemPedidoController {

    private final ItemPedidoService itemPedidoService;

    private final ItemPedidoMapper itemPedidoMapper;

    @Operation(summary = "Buscar item pedido por id usuário, id pedido e id item pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item pedido encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemPedidoResumidoDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Item pedido não encontrado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ItemPedidoResumidoDTO> buscarItemPedidoPorId(@PathVariable Long idUsuario, @PathVariable Long idPedido, @PathVariable Long id){
        log.debug("GET buscarItemPedidoPorId idUsuario : {}, idPedido : {}, idItemPedido : {}", idUsuario, idPedido, id);
        ItemPedido itemPedido = itemPedidoService.buscarItemPedidoPorId(idUsuario, idPedido, id);
        ItemPedidoResumidoDTO itemPedidoResumidoDTO = itemPedidoMapper.toItemPedidoResumidoDto(itemPedido);

        log.debug("GET buscarItemPedidoPorId itemPedido : {}", itemPedidoResumidoDTO);
        log.info("GET buscarItemPedidoPorId nomeProduto : {}", itemPedidoResumidoDTO.getProduto().getNome());
        return ResponseEntity.ok().body(itemPedidoResumidoDTO);
    }

    @Operation(summary = "Buscar itens pedidos por id usuário e id pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itens pedidos encontrados",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemPedidoResumidoDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Item pedido não encontrado",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<ItemPedidoResumidoDTO>> buscarItensPedidosPaginados(@PathVariable Long idUsuario,
                                                                                   @PathVariable Long idPedido,
                                                                                   @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        log.debug("GET buscarItensPedidosPaginados idUsuario : {}, idPedido : {}, pageable : {}", idUsuario, idPedido, pageable);
        Page<ItemPedido> itemPedidos = itemPedidoService.buscarItensPedidosPaginados(idUsuario, idPedido, pageable);
        Page<ItemPedidoResumidoDTO> itemPedidoResumidoDTOS = itemPedidoMapper.toItemPedidoResumidoDtos(itemPedidos);

        return ResponseEntity.ok().body(itemPedidoResumidoDTOS);
    }
}
