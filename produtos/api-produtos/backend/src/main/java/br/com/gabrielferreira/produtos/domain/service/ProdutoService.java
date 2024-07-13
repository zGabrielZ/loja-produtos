package br.com.gabrielferreira.produtos.domain.service;

import br.com.gabrielferreira.produtos.domain.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProdutoService {

    Produto salvarProduto(Produto produto);

    Produto buscarProdutoPorId(Long id);

    Produto atualizarProduto(Long id, Produto produto);

    void deletarProdutoPorId(Long id);

    Page<Produto> buscarProdutosPaginados(Pageable pageable, String nome, BigDecimal preco);

    boolean existeProdutoPorId(Long id);

    boolean naoExisteProdutoPorId(Long id);
}
