package br.com.gabrielferreira.produtos.domain.service.impl;

import br.com.gabrielferreira.produtos.domain.exception.NaoEncontradoException;
import br.com.gabrielferreira.produtos.domain.exception.RegraDeNegocioException;
import br.com.gabrielferreira.produtos.domain.model.Produto;
import br.com.gabrielferreira.produtos.domain.repository.ProdutoRepository;
import br.com.gabrielferreira.produtos.domain.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static br.com.gabrielferreira.produtos.domain.specification.ProdutoSpecification.*;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Transactional
    @Override
    public Produto salvarProduto(Produto produto) {
        validarCamposProduto(produto);
        validarNomeProduto(produto.getNome(), null);

        produto.adicionarHistoricoProduto();
        produto = produtoRepository.save(produto);
        return produto;
    }

    @Override
    public Produto buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Produto não encontrado", id));
    }

    @Transactional
    @Override
    public Produto atualizarProduto(Long id, Produto produto) {
        Produto produtoEncontrado = buscarProdutoPorId(id);

        validarCamposProduto(produto);
        validarNomeProduto(produto.getNome(), id);

        preencherCamposProduto(produtoEncontrado, produto);

        produtoEncontrado = produtoRepository.save(produtoEncontrado);
        produtoEncontrado.adicionarHistoricoProduto();
        return produtoEncontrado;
    }

    @Transactional
    @Override
    public void deletarProdutoPorId(Long id) {
        Produto produtoEncontrado = buscarProdutoPorId(id);
        produtoRepository.delete(produtoEncontrado);
    }

    @Override
    public Page<Produto> buscarProdutosPaginados(Pageable pageable, String nome, BigDecimal preco) {
        Specification<Produto> specification = Specification.where(
                buscarPorNome(nome)
                        .and(buscarPorPreco(preco))
        );

        return produtoRepository.findAll(specification, pageable);
    }

    @Override
    public boolean existeProdutoPorId(Long id) {
        return produtoRepository.existsProdutoPorId(id);
    }

    @Override
    public boolean naoExisteProdutoPorId(Long id) {
        return !existeProdutoPorId(id);
    }

    private void validarCamposProduto(Produto produto){
        produto.setNome(produto.getNome().trim());
    }

    private void validarNomeProduto(String nome, Long id){
        if(isNomeExistente(nome, id)){
            throw new RegraDeNegocioException(String.format("Não vai ser possível cadastrar este produto pois o nome '%s' já foi cadastrado", nome));
        }
    }

    private boolean isNomeExistente(String nome, Long id){
        if(id == null){
            return produtoRepository.buscarPorNome(nome)
                    .isPresent();
        } else {
            return produtoRepository.buscarPorNome(nome)
                    .filter(p -> !p.getId().equals(id))
                    .isPresent();
        }
    }

    private void preencherCamposProduto(Produto produtoEncontrado, Produto produto){
        produtoEncontrado.setNome(produto.getNome());
        produtoEncontrado.setPreco(produto.getPreco());
    }
}
