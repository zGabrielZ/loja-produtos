package br.com.gabrielferreira.produtos.api.mapper;

import br.com.gabrielferreira.produtos.api.dto.ProdutoDTO;
import br.com.gabrielferreira.produtos.api.dto.create.ProdutoCreateDTO;
import br.com.gabrielferreira.produtos.api.dto.update.ProdutoUpdateDTO;
import br.com.gabrielferreira.produtos.domain.model.Produto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = {FormatMapper.class})
public interface ProdutoMapper {

    Produto toProduto(ProdutoCreateDTO produtoCreateDTO);

    @BeanMapping(builder = @Builder( disableBuilder = true ))
    Produto toProduto(ProdutoUpdateDTO produtoUpdateDTO);

    @Mapping(target = "dataInclusao", qualifiedByName = "formatData")
    @Mapping(target = "dataAtualizacao", qualifiedByName = "formatData")
    ProdutoDTO toProdutoDto(Produto produto);

    default Page<ProdutoDTO> toProdutosDtos(Page<Produto> produtos){
        return produtos.map(this::toProdutoDto);
    }
}
