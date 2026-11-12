
package com.agendamentos.backend.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados públicos do cadastro profissional")
public record ProfissionalResponse(

    Long id,

    Long usuarioId,

    String nomeExibicao,

    Boolean ativo,

    LocalDateTime criadoEm

) {
}
