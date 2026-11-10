package com.agendamentos.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados necessários para cadastrar um usuário")
public record CriarUsuarioRequest(

    @Schema(
        description = "Nome completo do usuário",
        example = "Maria Silva"
    )
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    String nome,

    @Schema(
        description = "E-mail do usuário",
        example = "maria@exemplo.com"
    )
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    @Size(max = 180, message = "O e-mail deve ter no máximo 180 caracteres")
    String email,

    @Schema(
        description = "Senha de acesso, com no mínimo 8 caracteres",
        example = "MinhaSenha123",
        format = "password"
    )
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    String senha

) {}
