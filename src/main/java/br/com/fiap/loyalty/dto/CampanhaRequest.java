package br.com.fiap.loyalty.dto;

import br.com.fiap.loyalty.util.Textos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

// Corpo do POST /empresas/{idEmpresa}/campanhas e do PUT /campanhas/{id}.
// A regra "data de fim depois da de início" é checada no CampanhaBO
public record CampanhaRequest(
        @NotBlank(message = "Informe o nome da campanha.")
        @Size(max = 100, message = "O nome da campanha deve ter até 100 caracteres.")
        String nome,

        @Size(max = 500, message = "A descrição deve ter até 500 caracteres.")
        String descricao,

        @NotNull(message = "Informe a data de início.")
        LocalDate dataInicio,

        // Opcional: campanha sem data de fim é contínua
        LocalDate dataFim) {

    public CampanhaRequest {
        nome = Textos.limpar(nome);
        descricao = Textos.limpar(descricao);
    }
}
