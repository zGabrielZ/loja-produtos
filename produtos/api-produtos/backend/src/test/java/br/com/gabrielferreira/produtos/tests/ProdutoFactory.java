package br.com.gabrielferreira.produtos.tests;

import br.com.gabrielferreira.produtos.api.dto.create.ProdutoCreateDTO;
import br.com.gabrielferreira.produtos.api.dto.update.ProdutoUpdateDTO;
import br.com.gabrielferreira.produtos.domain.model.Produto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class ProdutoFactory {

    private ProdutoFactory(){}

    public static ProdutoCreateDTO criarProduto(){
        return ProdutoCreateDTO.builder()
                .nome("Laranja")
                .preco(BigDecimal.valueOf(4.00))
                .build();
    }

    public static ProdutoUpdateDTO atualizarProduto(){
        ProdutoUpdateDTO produtoUpdateDTO = new ProdutoUpdateDTO();
        produtoUpdateDTO.setNome("Laranja Limão");
        produtoUpdateDTO.setPreco(BigDecimal.valueOf(10.00));
        return produtoUpdateDTO;
    }

    public static Produto criarProdutoLaranja(Long id){
        return Produto.builder()
                .id(id)
                .nome("Laranja")
                .preco(BigDecimal.ONE)
                .itensPedidos(new ArrayList<>())
                .historicoProdutos(new ArrayList<>())
                .dataInclusao(ZonedDateTime.now())
                .build();
    }

    public static Produto criarProdutoLimao(Long id){
        return Produto.builder()
                .id(id)
                .nome("Limão")
                .preco(BigDecimal.ONE)
                .itensPedidos(new ArrayList<>())
                .historicoProdutos(new ArrayList<>())
                .dataInclusao(ZonedDateTime.now())
                .build();
    }
}
