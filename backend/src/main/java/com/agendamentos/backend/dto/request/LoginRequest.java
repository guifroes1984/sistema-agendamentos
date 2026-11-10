package com.agendamentos.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais para autenticação do usuário")
public record LoginRequest(

    @Schema(
        description = "E-mail cadastrado do usuário",
        example = "maria@exemplo.com"
    )
    @NotBlank (message = "O e-mail é obrigatório")
    @Email (message = "Informe um e-mail válido")
    String email,

    @Schema(
        description = "Senha de acesso do usuário",
        example = "MinhaSenha123",
        format = "password"
    )
    @NotBlank(message = "A senha é obrigatória")
    String senha

) {}
