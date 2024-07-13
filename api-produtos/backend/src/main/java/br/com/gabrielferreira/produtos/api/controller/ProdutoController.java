package br.com.gabrielferreira.produtos.api.controller;

import br.com.gabrielferreira.produtos.api.dto.ProdutoDTO;
import br.com.gabrielferreira.produtos.api.dto.create.ProdutoCreateDTO;
import br.com.gabrielferreira.produtos.api.dto.update.ProdutoUpdateDTO;
import br.com.gabrielferreira.produtos.api.mapper.ProdutoMapper;
import br.com.gabrielferreira.produtos.domain.model.Produto;
import br.com.gabrielferreira.produtos.domain.service.ProdutoService;
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

import java.math.BigDecimal;
import java.net.URI;

@Tag(name = "Produto Controller", description = "Endpoints para realizar requisições de produto")
@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
@Log4j2
public class ProdutoController {

    private final ProdutoService produtoService;

    private final ProdutoMapper produtoMapper;

    @Operation(summary = "Cadastrar produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto cadastrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Regra de negócio",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProdutoDTO> cadastrarProduto(@Valid @RequestBody ProdutoCreateDTO produtoCreateDTO){
        log.debug("POST cadastrarProduto produto : {}", produtoCreateDTO);
        Produto produto = produtoMapper.toProduto(produtoCreateDTO);
        Produto produtoCadastrado = produtoService.salvarProduto(produto);
        ProdutoDTO produtoDTO = produtoMapper.toProdutoDto(produtoCadastrado);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(produtoDTO.getId()).toUri();
        log.debug("POST cadastrarPedido salvo : {}", produtoDTO);
        log.info("POST cadastrarPedido salvo idProduto : {}", produtoDTO.getId());
        return ResponseEntity.created(uri).body(produtoDTO);
    }

    @Operation(summary = "Buscar produto por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable Long id){
        log.debug("GET buscarProdutoPorId idProduto : {}", id);
        Produto produto = produtoService.buscarProdutoPorId(id);
        ProdutoDTO produtoDTO = produtoMapper.toProdutoDto(produto);

        log.debug("GET buscarProdutoPorId produto : {}", produtoDTO);
        log.info("GET buscarProdutoPorId nomeProduto : {}", produtoDTO.getNome());
        return ResponseEntity.ok().body(produtoDTO);
    }

    @Operation(summary = "Atualizar produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Regra de negócio",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Produto não encontrado",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizarProdutoPorId(@PathVariable Long id, @Valid @RequestBody ProdutoUpdateDTO produtoUpdateDTO){
        log.debug("PUT atualizarProdutoPorId idProduto : {}, produto : {}", id, produtoUpdateDTO);
        Produto produto = produtoMapper.toProduto(produtoUpdateDTO);
        Produto produtoAtualizado = produtoService.atualizarProduto(id, produto);
        ProdutoDTO produtoDTO = produtoMapper.toProdutoDto(produtoAtualizado);

        log.debug("PUT atualizarProdutoPorId atualizado produto : {}", produtoDTO);
        log.info("PUT atualizarProdutoPorId atualizado idProduto : {}", produtoDTO.getId());
        return ResponseEntity.ok().body(produtoDTO);
    }

    @Operation(summary = "Deletar produto por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProdutoPorId(@PathVariable Long id){
        log.debug("DELETE deletarProdutoPorId idProduto : {}", id);
        produtoService.deletarProdutoPorId(id);

        log.debug("DELETE deletarProdutoPorId deletado idProduto : {}", id);
        log.info("DELETE deletarProdutoPorId deletado idProduto : {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar produtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoDTO.class)) })
    })
    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>> buscarProdutosPaginados(@PageableDefault(size = 5, sort = "dataInclusao", direction = Sort.Direction.DESC) Pageable pageable,
                                                                    @RequestParam(required = false) String nome,
                                                                    @RequestParam(required = false) BigDecimal preco){
        log.debug("GET buscarProdutosPaginados nome : {}, preco : {}, pageable : {}", nome, preco, pageable);
        Page<Produto> produtos = produtoService.buscarProdutosPaginados(pageable, nome, preco);
        Page<ProdutoDTO> produtosDtos = produtoMapper.toProdutosDtos(produtos);
        return ResponseEntity.ok().body(produtosDtos);
    }
}
