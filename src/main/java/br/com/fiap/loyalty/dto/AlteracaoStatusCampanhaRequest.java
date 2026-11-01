package br.com.fiap.loyalty.dto;

import br.com.fiap.loyalty.model.StatusCampanha;
import jakarta.validation.constraints.NotNull;

// Corpo do PATCH /campanhas/{id}/status
public record AlteracaoStatusCampanhaRequest(
        @NotNull(message = "Informe o status: ATIVA, INATIVA ou ENCERRADA.")
        StatusCampanha status) {
}
