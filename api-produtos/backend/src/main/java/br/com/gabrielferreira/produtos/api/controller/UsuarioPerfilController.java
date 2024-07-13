package br.com.gabrielferreira.produtos.api.controller;

import br.com.gabrielferreira.produtos.api.dto.PerfilDTO;
import br.com.gabrielferreira.produtos.api.mapper.PerfilMapper;
import br.com.gabrielferreira.produtos.domain.model.Perfil;
import br.com.gabrielferreira.produtos.domain.service.UsuarioPerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Usuário Perfil Controller", description = "Endpoints para realizar requisições de perfis do usuário")
@RestController
@RequestMapping("/usuarios/{idUsuario}/perfis")
@RequiredArgsConstructor
@Log4j2
public class UsuarioPerfilController {

    private final UsuarioPerfilService usuarioPerfilService;

    private final PerfilMapper perfilMapper;

    @Operation(summary = "Buscar perfil por id usuário e id perfil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PerfilDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PerfilDTO> buscarPerfilPorId(@PathVariable Long idUsuario,@PathVariable Long id){
        log.debug("GET buscarPerfilPorId idUsuario : {}, idPerfil : {}", idUsuario, id);
        Perfil perfil = usuarioPerfilService.buscarPerfilPorId(idUsuario, id);
        PerfilDTO perfilDTO = perfilMapper.toPerfilDto(perfil);

        log.debug("GET buscarPerfilPorId perfil : {}", perfilDTO);
        log.info("GET buscarPerfilPorId nomePerfil : {}", perfilDTO.getDescricao());
        return ResponseEntity.ok().body(perfilDTO);
    }

    @Operation(summary = "Buscar perfis por id usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfis encontrados",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PerfilDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PerfilDTO>> buscarPerfis(@PathVariable Long idUsuario){
        log.debug("GET buscarPerfis idUsuario : {}", idUsuario);
        List<Perfil> perfis = usuarioPerfilService.buscarPerfisPorIdUsuario(idUsuario);
        List<PerfilDTO> perfilDTO = perfilMapper.toPerfisDtos(perfis);

        return ResponseEntity.ok().body(perfilDTO);
    }
}
