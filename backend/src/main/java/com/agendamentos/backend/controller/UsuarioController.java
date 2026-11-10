package com.agendamentos.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agendamentos.backend.dto.request.CriarUsuarioRequest;
import com.agendamentos.backend.dto.response.UsuarioResponse;
import com.agendamentos.backend.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Operações relacionadas ao gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Cadastrar usuário", description = "Realiza o cadastro de um novo usuário no sistema.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de cadastro inválidos", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "mensagem": "Dados inválidos.",
                      "erros": {
                        "nome": "O nome é obrigatório",
                        "email": "Informe um e-mail válido",
                        "senha": "A senha deve ter no mínimo 8 caracteres"
                      }
                    }
                    """))),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "mensagem": "Este e-mail já está cadastrado."
                    }
                    """)))
    })
    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody CriarUsuarioRequest request) {

        UsuarioResponse usuario = usuarioService.cadastrar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

}
