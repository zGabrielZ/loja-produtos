package br.com.gabrielferreira.notificacao.api.dto;

import br.com.gabrielferreira.notificacao.domain.model.enums.NotificacaoStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoResumidoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1986606901013191527L;

    @Schema(description = "Id da notificação", example = "1")
    private Long id;

    @Schema(description = "Nome do remetente", example = "Loja virtual")
    private String nomeRemetente;

    @Schema(description = "Título da notificação", example = "Pedido realizado com sucesso")
    private String titulo;

    @Schema(description = "Status da notificação", example = "SUCESSO")
    private NotificacaoStatusEnum status;
}
