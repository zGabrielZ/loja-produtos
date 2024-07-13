package br.com.gabrielferreira.produtos.domain.service.impl;

import br.com.gabrielferreira.produtos.domain.exception.NaoEncontradoException;
import br.com.gabrielferreira.produtos.domain.model.HistoricoProduto;
import br.com.gabrielferreira.produtos.domain.repository.HistoricoProdutoRepository;
import br.com.gabrielferreira.produtos.domain.service.HistoricoProdutoService;
import br.com.gabrielferreira.produtos.domain.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoricoProdutoServiceImpl implements HistoricoProdutoService {

    private final HistoricoProdutoRepository historicoProdutoRepository;

    private final ProdutoService produtoService;

    @Override
    public Page<HistoricoProduto> buscarHistoricoPaginadosPorIdProduto(Long idProduto, Pageable pageable) {
        validarProdutoExistente(idProduto);
        return historicoProdutoRepository.buscarHistoricosProdutosPorIdProduto(idProduto, pageable);
    }

    @Override
    public HistoricoProduto buscarHistoricoProdutoPorId(Long idProduto, Long idHistoricoProduto) {
        validarProdutoExistente(idProduto);
        return historicoProdutoRepository.buscarHistoricoProdutoPorId(idProduto, idHistoricoProduto)
                .orElseThrow(() -> new NaoEncontradoException("Histórico produto não encontrado", idProduto, idHistoricoProduto));
    }

    private void validarProdutoExistente(Long idProduto){
        if(produtoService.naoExisteProdutoPorId(idProduto)){
            throw new NaoEncontradoException("Produto não encontrado", idProduto);
        }
    }
}
