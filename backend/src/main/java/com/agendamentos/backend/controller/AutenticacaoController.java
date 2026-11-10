package com.agendamentos.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agendamentos.backend.dto.request.LoginRequest;
import com.agendamentos.backend.dto.response.LoginResponse;
import com.agendamentos.backend.service.AutenticacaoService;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Operações relacionadas à autenticação de usuários")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(
            AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @Operation(summary = "Autenticar usuário", description = "Valida e-mail e senha e retorna um token JWT.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
    @ApiResponse(responseCode = "400", description = "Dados de login inválidos"),
    @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
        {
            "mensagem": "E-mail ou senha inválidos."
        }
        """)))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse resposta = autenticacaoService.autenticar(request);

        return ResponseEntity.ok(resposta);
    }
}
