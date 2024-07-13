package br.com.gabrielferreira.produtos.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplateEnum {

    PEDIDOS("pedidos");

    private final String templateEmail;
}
