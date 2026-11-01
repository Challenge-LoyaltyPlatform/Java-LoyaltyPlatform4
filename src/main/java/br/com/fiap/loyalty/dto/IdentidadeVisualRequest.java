package br.com.fiap.loyalty.dto;

import br.com.fiap.loyalty.model.FormaBotoes;
import br.com.fiap.loyalty.model.IdentidadeVisual;
import br.com.fiap.loyalty.model.Tipografia;
import br.com.fiap.loyalty.util.Textos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Locale;

// Personalização white label enviada no cadastro e na edição da empresa
public record IdentidadeVisualRequest(
        @NotBlank(message = "Informe a cor principal.")
        @Pattern(regexp = "#[0-9A-F]{6}", message = "Use uma cor no formato hexadecimal #RRGGBB.")
        String corPrincipal,

        @NotNull(message = "Escolha a tipografia: SORA, INTER ou POPPINS.")
        Tipografia tipografia,

        @NotNull(message = "Escolha o formato dos botões: PILULA, ARREDONDADO ou RETO.")
        FormaBotoes formaBotoes) {

    public IdentidadeVisualRequest {
        corPrincipal = Textos.limpar(corPrincipal);
        if (corPrincipal != null) {
            corPrincipal = corPrincipal.toUpperCase(Locale.ROOT);
        }
    }

    public IdentidadeVisual paraModelo() {
        return new IdentidadeVisual(corPrincipal, tipografia, formaBotoes);
    }
}
