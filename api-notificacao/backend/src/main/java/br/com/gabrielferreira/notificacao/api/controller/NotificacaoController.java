package br.com.gabrielferreira.notificacao.api.controller;

import br.com.gabrielferreira.notificacao.api.dto.NotificacaoResumidoDTO;
import br.com.gabrielferreira.notificacao.api.mapper.NotificacaoDTOMapper;
import br.com.gabrielferreira.notificacao.domain.model.Notificacao;
import br.com.gabrielferreira.notificacao.domain.model.enums.NotificacaoStatusEnum;
import br.com.gabrielferreira.notificacao.domain.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Notificação Controller", description = "Endpoints para busca de notificações")
@RestController
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
@Log4j2
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    private final NotificacaoDTOMapper notificacaoDTOMapper;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Buscar notificações paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificações encontrados",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificacaoResumidoDTO.class)) })
    })
    @GetMapping
    public ResponseEntity<Page<NotificacaoResumidoDTO>> buscarNotificacoesPaginados(String titulo,
                                                                                    NotificacaoStatusEnum status,
                                                                                    @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        log.debug("GET buscarNotificacoesPaginados titulo : {}, status : {}, pageable : {}", titulo, status, pageable);
        Page<Notificacao> notificacoes = notificacaoService.buscarNotificacoes(titulo, status, pageable);
        Page<NotificacaoResumidoDTO> notificacaoResumidoDTOS = notificacaoDTOMapper.toNotificacoesDtos(notificacoes);
        return ResponseEntity.ok().body(notificacaoResumidoDTOS);
    }
}
