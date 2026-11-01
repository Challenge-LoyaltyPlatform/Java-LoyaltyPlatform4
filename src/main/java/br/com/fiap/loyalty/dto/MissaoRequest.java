package br.com.fiap.loyalty.dto;

import br.com.fiap.loyalty.util.Textos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

// Corpo do POST /campanhas/{idCampanha}/missoes e do PUT /missoes/{id}
public record MissaoRequest(
        @NotBlank(message = "Informe o nome da missão.")
        @Size(max = 100, message = "O nome da missão deve ter até 100 caracteres.")
        String nome,

        @Size(max = 400, message = "A descrição deve ter até 400 caracteres.")
        String descricao,

        // RN06 — meta e pontos são inteiros positivos
        @NotNull(message = "Informe a meta da missão.")
        @Positive(message = "A meta deve ser um número inteiro maior que zero.")
        Integer meta,

        @NotNull(message = "Informe os pontos de recompensa.")
        @Positive(message = "Os pontos devem ser um número inteiro maior que zero.")
        Integer pontosRecompensa) {

    public MissaoRequest {
        nome = Textos.limpar(nome);
        descricao = Textos.limpar(descricao);
    }
}
