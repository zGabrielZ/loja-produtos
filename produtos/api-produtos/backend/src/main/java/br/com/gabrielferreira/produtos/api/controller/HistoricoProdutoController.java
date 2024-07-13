package br.com.gabrielferreira.produtos.api.controller;

import br.com.gabrielferreira.produtos.api.dto.HistoricoProdutoDTO;
import br.com.gabrielferreira.produtos.api.mapper.HistoricoProdutoMapper;
import br.com.gabrielferreira.produtos.domain.model.HistoricoProduto;
import br.com.gabrielferreira.produtos.domain.service.HistoricoProdutoService;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Historico Produto Controller", description = "Endpoints para busca de históricos de produtos")
@RestController
@RequestMapping("/produtos/{idProduto}/historicos")
@RequiredArgsConstructor
@Log4j2
public class HistoricoProdutoController {

    private final HistoricoProdutoService historicoProdutoService;

    private final HistoricoProdutoMapper historicoProdutoMapper;

    @Operation(summary = "Buscar históricos produtos por id produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Históricos produtos encontrados",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HistoricoProdutoDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<HistoricoProdutoDTO>> buscarHistoricoProdutosPaginados(@PathVariable Long idProduto,
                                                                                      @PageableDefault(size = 5, sort = "dataInclusao", direction = Sort.Direction.DESC) Pageable pageable){
        log.debug("GET buscarHistoricoProdutosPaginados idProduto : {}, pageable : {}", idProduto, pageable);
        Page<HistoricoProduto> historicoProdutos = historicoProdutoService.buscarHistoricoPaginadosPorIdProduto(idProduto, pageable);
        Page<HistoricoProdutoDTO> historicoProdutoDTOS = historicoProdutoMapper.toHistoricoProdutosDtos(historicoProdutos);

        return ResponseEntity.ok().body(historicoProdutoDTOS);
    }

    @Operation(summary = "Buscar histórico produto por id produto e id histórico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico produto encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HistoricoProdutoDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<HistoricoProdutoDTO> buscarProdutoPorId(@PathVariable Long idProduto, @PathVariable Long id){
        log.debug("GET buscarProdutoPorId idProduto : {}, idHistorico : {}", idProduto, id);
        HistoricoProduto historicoProduto = historicoProdutoService.buscarHistoricoProdutoPorId(idProduto, id);
        HistoricoProdutoDTO historicoProdutoDTO = historicoProdutoMapper.toHistoricoProdutoDto(historicoProduto);

        log.debug("GET buscarProdutoPorId historicoProduto : {}", historicoProdutoDTO);
        log.info("GET buscarProdutoPorId nomeHistoricoProduto : {}", historicoProdutoDTO.getNome());
        return ResponseEntity.ok().body(historicoProdutoDTO);
    }
}
