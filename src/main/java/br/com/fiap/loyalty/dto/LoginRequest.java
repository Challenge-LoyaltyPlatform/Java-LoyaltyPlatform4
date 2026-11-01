package br.com.fiap.loyalty.dto;

import br.com.fiap.loyalty.util.Textos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Corpo do POST /auth/login
public record LoginRequest(
        @NotBlank(message = "Informe o e-mail.")
        @Email(message = "Informe um e-mail válido.")
        String email,

        @NotBlank(message = "Informe a senha.")
        String senha) {

    public LoginRequest {
        email = Textos.emailNormalizado(email);
    }
}
