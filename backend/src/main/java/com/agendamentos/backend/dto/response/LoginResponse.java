package com.agendamentos.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados retornados após a autenticação do usuário")
public record LoginResponse(

    @Schema(
        description = "Token JWT utilizado para autenticar as requisições",
        example = "eyJhbGciOiJIUzI1NiJ9..."
    )
    String token,

    @Schema(
        description = "Tipo de autenticação utilizado",
        example = "Bearer"
    )
    String tipo,

    @Schema(
        description = "Tempo de validade do token em segundos",
        example = "3600"
    )
    Long expiraEm

) {}
