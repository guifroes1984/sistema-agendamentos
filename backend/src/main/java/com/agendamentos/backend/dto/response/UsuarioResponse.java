package com.agendamentos.backend.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados retornados após o cadastro de um usuário")
public record UsuarioResponse(

    @Schema(
        description = "Identificador único do usuário",
        example = "1"
    )
    Long id,

    @Schema(
        description = "Nome completo do usuário",
        example = "Maria Silva"
    )
    String nome,

    @Schema(
        description = "E-mail cadastrado",
        example = "maria@exemplo.com"
    )
    String email,

    @Schema(
        description = "Indica se o usuário está ativo",
        example = "true"
    )
    Boolean ativo,

    @Schema(
        description = "Data e hora de criação do usuário",
        example = "2026-11-09T19:43:35"
    )
    LocalDateTime criadoEm

) {}
