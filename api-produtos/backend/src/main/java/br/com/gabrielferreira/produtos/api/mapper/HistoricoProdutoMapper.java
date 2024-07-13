package br.com.gabrielferreira.produtos.api.mapper;

import br.com.gabrielferreira.produtos.api.dto.HistoricoProdutoDTO;
import br.com.gabrielferreira.produtos.domain.model.HistoricoProduto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = {FormatMapper.class})
public interface HistoricoProdutoMapper {

    @Mapping(target = "dataInclusao", qualifiedByName = "formatData")
    HistoricoProdutoDTO toHistoricoProdutoDto(HistoricoProduto historicoProduto);

    default Page<HistoricoProdutoDTO> toHistoricoProdutosDtos(Page<HistoricoProduto> historicoProdutos){
        return historicoProdutos.map(this::toHistoricoProdutoDto);
    }
}
