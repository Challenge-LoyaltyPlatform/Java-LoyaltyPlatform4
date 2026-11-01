package br.com.fiap.loyalty.dto;

import br.com.fiap.loyalty.util.Textos;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Corpo do POST /empresas e do PUT /empresas/{id}.
// O front já valida, mas a API valida de novo porque o front pode ser burlado
public record EmpresaRequest(
        @NotBlank(message = "Informe a razão social.")
        @Size(max = 150, message = "A razão social deve ter até 150 caracteres.")
        String razaoSocial,

        @NotBlank(message = "Informe o nome fantasia.")
        @Size(max = 100, message = "O nome fantasia deve ter até 100 caracteres.")
        String nomeFantasia,

        // Só o formato: 12 letras ou números + 2 dígitos (aceita o CNPJ alfanumérico)
        @NotBlank(message = "Informe o CNPJ.")
        @Pattern(regexp = "[0-9A-Z]{12}[0-9]{2}",
                message = "O CNPJ deve ter 14 caracteres: 12 letras ou números seguidos de 2 dígitos.")
        String cnpj,

        @NotBlank(message = "Informe o e-mail.")
        @Email(regexp = "[^\\s@]+@[^\\s@]+\\.[^\\s@]+", message = "Informe um e-mail válido.")
        @Size(max = 100, message = "O e-mail deve ter até 100 caracteres.")
        String email,

        // Opcional: só dígitos com DDD
        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve ter DDD e 10 ou 11 dígitos.")
        String telefone,

        @NotNull(message = "Informe a identidade visual.")
        @Valid
        IdentidadeVisualRequest identidadeVisual) {

    public EmpresaRequest {
        razaoSocial = Textos.limpar(razaoSocial);
        nomeFantasia = Textos.limpar(nomeFantasia);
        cnpj = Textos.cnpjSemMascara(cnpj);
        email = Textos.emailNormalizado(email);
        telefone = Textos.somenteDigitos(telefone);
    }
}
