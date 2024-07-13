package br.com.gabrielferreira.produtos.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.ZonedDateTime;

import static br.com.gabrielferreira.produtos.common.utils.DataUtils.*;

@Mapper(componentModel = "spring")
public interface FormatMapper {

    @Named("formatData")
    default ZonedDateTime formatDate(ZonedDateTime data){
        return toFusoPadraoSistema(data);
    }
}
