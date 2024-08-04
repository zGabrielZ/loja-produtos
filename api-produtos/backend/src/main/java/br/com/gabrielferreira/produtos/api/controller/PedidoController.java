package br.com.gabrielferreira.produtos.api.controller;

import br.com.gabrielferreira.produtos.api.dto.PedidoDTO;
import br.com.gabrielferreira.produtos.api.dto.PedidoResumidoDTO;
import br.com.gabrielferreira.produtos.api.dto.create.PedidoCreateDTO;
import br.com.gabrielferreira.produtos.api.mapper.PedidoMapper;
import br.com.gabrielferreira.produtos.domain.model.Pedido;
import br.com.gabrielferreira.produtos.domain.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Pedido Controller", description = "Endpoints para realizar requisições de pedidos")
@RestController
@RequestMapping("/usuarios/{idUsuario}/pedidos")
@RequiredArgsConstructor
@Log4j2
public class PedidoController {

    private final PedidoService pedidoService;

    private final PedidoMapper pedidoMapper;

    @Operation(summary = "Cadastrar pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido cadastrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Recurso não encontrado",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<PedidoDTO> cadastrarPedido(@PathVariable Long idUsuario, @Valid @RequestBody PedidoCreateDTO pedidoCreateDTO){
        log.debug("POST cadastrarPedido idUsuario : {}, pedido : {}", idUsuario, pedidoCreateDTO);
        Pedido pedido = pedidoMapper.toPedido(pedidoCreateDTO);
        Pedido pedidoCadastrado = pedidoService.salvarPedidoEnviarNotificacao(idUsuario, pedido);
        PedidoDTO pedidoDTO = pedidoMapper.toPedidoDto(pedidoCadastrado);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(pedidoDTO.getId()).toUri();

        log.debug("POST cadastrarPedido salvo : {}", pedidoDTO);
        log.info("POST cadastrarPedido salvo idPedido : {}", pedidoDTO.getId());
        return ResponseEntity.created(uri).body(pedidoDTO);
    }

    @Operation(summary = "Buscar pedido por id do usuário e id do pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResumidoDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResumidoDTO> buscarPedidoPorId(@PathVariable Long idUsuario, @PathVariable Long id){
        log.debug("GET buscarPedidoPorId idUsuario : {}, idPedido : {}", idUsuario, id);
        Pedido pedido = pedidoService.buscarPedidoPorId(idUsuario, id);
        PedidoResumidoDTO pedidoResumidoDTO = pedidoMapper.toPedidoResumidoDto(pedido);

        log.debug("GET buscarPedidoPorId pedido : {}", pedidoResumidoDTO);
        log.info("GET buscarPedidoPorId idPedido : {}", pedidoResumidoDTO.getId());
        return ResponseEntity.ok().body(pedidoResumidoDTO);
    }

    @Operation(summary = "Finalizar pedido por id usuario e id do pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido finalizado",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Regra de negócio",
                    content = @Content)
    })
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarPedidoPorId(@PathVariable Long idUsuario, @PathVariable Long id){
        log.debug("PUT finalizarPedidoPorId idUsuario : {}, idPedido : {}", idUsuario, id);
        pedidoService.finalizarPedidoPorIdEnviarNotificacao(idUsuario, id);

        log.debug("PUT finalizarPedidoPorId atualizado idPedido : {}", id);
        log.info("PUT finalizarPedidoPorId atualizado idPedido : {}", id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cancelar pedido por id usuario e id do pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido cancelado",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Regra de negócio",
                    content = @Content)
    })
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedidoPorId(@PathVariable Long idUsuario, @PathVariable Long id){
        log.debug("PUT cancelarPedidoPorId idUsuario : {}, idPedido : {}", idUsuario, id);
        pedidoService.cancelarPedidoPorIdEnviarNotificacao(idUsuario, id);

        log.debug("PUT cancelarPedidoPorId atualizado idPedido : {}", id);
        log.info("PUT cancelarPedidoPorId atualizado idPedido : {}", id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Buscar pedidos por id usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResumidoDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Usuário não encontrado",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<PedidoResumidoDTO>> buscarPedidosPaginados(@PathVariable Long idUsuario,
                                                                          @PageableDefault(size = 5, sort = "dataInclusao", direction = Sort.Direction.DESC) Pageable pageable){
        log.debug("GET buscarPedidosPaginados idUsuario : {}, pageable : {}", idUsuario, pageable);
        Page<Pedido> pedidos = pedidoService.buscarPedidosPaginados(idUsuario, pageable);
        Page<PedidoResumidoDTO> pedidoResumidoDTOS = pedidoMapper.toPedidoResumidoDtos(pedidos);

        return ResponseEntity.ok().body(pedidoResumidoDTOS);
    }
}
