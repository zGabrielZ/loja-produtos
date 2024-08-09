package br.com.gabrielferreira.produtos.api.controller;

import br.com.gabrielferreira.produtos.api.dto.AuthDTO;
import br.com.gabrielferreira.produtos.api.dto.TokenDTO;
import br.com.gabrielferreira.produtos.api.mapper.TokenMapper;
import br.com.gabrielferreira.produtos.api.mapper.UsernamePasswordAuthenticationMapper;
import br.com.gabrielferreira.produtos.common.config.security.UserDetailsImpl;
import br.com.gabrielferreira.produtos.common.config.security.service.TokenService;
import br.com.gabrielferreira.produtos.common.config.security.service.UserDetailsAutenticacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação Controller", description = "Endpoints para realizar requisições da autenticação")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Log4j2
public class AutenticacaoController {

    private final UsernamePasswordAuthenticationMapper usernamePasswordAuthenticationMapper;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final TokenMapper tokenMapper;

    private final UserDetailsAutenticacaoService userDetailsAutenticacaoService;

    @Operation(summary = "Autenticar usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Regra de negócio",
                    content = @Content),
    })
    @PostMapping
    public ResponseEntity<TokenDTO> autenticar(@Valid @RequestBody AuthDTO authDTO){
        log.debug("POST autenticar authDTO : {}", authDTO.getEmail());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = usernamePasswordAuthenticationMapper
                .toUsernamePasswordAuthenticationToken(authDTO);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        String tokenGerado = tokenService.gerarToken((UserDetailsImpl) authentication.getPrincipal());
        TokenDTO tokenDTO = tokenMapper.toTokenDTO(tokenGerado, "Bearer");
        log.info("POST autenticar token gerado");
        return ResponseEntity.ok(tokenDTO);
    }

    @Operation(summary = "Refresh token do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token atualizado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Regra de negócio",
                    content = @Content),
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenDTO> refreshToken(){
        log.debug("POST refreshToken");
        UserDetailsImpl userDetails = userDetailsAutenticacaoService.buscarUsuarioAutenticado();

        log.debug("E-mail do usuário encontrado {}", userDetails.getEmail());

        String tokenGerado = tokenService.gerarToken(userDetails);
        TokenDTO tokenDTO = tokenMapper.toTokenDTO(tokenGerado, "Bearer");
        log.info("POST autenticar token gerado");
        return ResponseEntity.ok(tokenDTO);
    }
}
