package br.com.gabrielferreira.produtos.commons.dto;

import br.com.gabrielferreira.produtos.commons.enums.EmailTemplateEnum;
import lombok.*;

import java.io.Serial;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotificacaoDTO extends ErroDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nomeRemetente;

    private String titulo;

    private String[] destinatarios;

    private EmailTemplateEnum emailTemplate;

    private transient Map<String, Object> dados;
}
