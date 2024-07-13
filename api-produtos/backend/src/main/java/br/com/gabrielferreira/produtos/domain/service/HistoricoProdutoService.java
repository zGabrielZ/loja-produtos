package br.com.gabrielferreira.produtos.domain.service;

import br.com.gabrielferreira.produtos.domain.model.HistoricoProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoricoProdutoService {

    Page<HistoricoProduto> buscarHistoricoPaginadosPorIdProduto(Long idProduto, Pageable pageable);

    HistoricoProduto buscarHistoricoProdutoPorId(Long idProduto, Long idHistoricoProduto);
}
