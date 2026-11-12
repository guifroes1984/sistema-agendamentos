
package com.agendamentos.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

public record CriarProfissionalRequest(

    @Schema(
        description = "Nome exibido na agenda do profissional",
        example = "Francilaine"
    )
    @NotBlank(message = "O nome de exibição é obrigatório")
    @Size(
        max = 150,
        message = "O nome de exibição deve ter no máximo 150 caracteres"
    )
    String nomeExibicao

) {
}
