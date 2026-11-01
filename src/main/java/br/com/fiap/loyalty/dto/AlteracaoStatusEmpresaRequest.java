package br.com.fiap.loyalty.dto;

import br.com.fiap.loyalty.model.StatusEmpresa;
import jakarta.validation.constraints.NotNull;

// Corpo do PATCH /empresas/{id}/status
public record AlteracaoStatusEmpresaRequest(
        @NotNull(message = "Informe o status: ATIVA ou INATIVA.")
        StatusEmpresa status) {
}
