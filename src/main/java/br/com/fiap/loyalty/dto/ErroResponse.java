package br.com.fiap.loyalty.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

// Corpo de toda resposta 4xx e 5xx. O front mostra "mensagem" e marca cada campo de "campos"
public record ErroResponse(
        int status,
        String mensagem,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) Map<String, String> campos) {

    public ErroResponse(int status, String mensagem) {
        this(status, mensagem, Map.of());
    }
}
