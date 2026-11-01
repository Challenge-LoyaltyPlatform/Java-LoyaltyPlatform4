package br.com.fiap.loyalty.dto;

import br.com.fiap.loyalty.model.StatusMissao;
import jakarta.validation.constraints.NotNull;

// Corpo do PATCH /missoes/{id}/status. Para excluir, o front usa DELETE /missoes/{id}
public record AlteracaoStatusMissaoRequest(
        @NotNull(message = "Informe o status: ATIVA ou INATIVA.")
        StatusMissao status) {
}
