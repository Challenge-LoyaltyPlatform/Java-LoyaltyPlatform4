package br.com.fiap.loyalty.util;

import java.util.Locale;

// Normalização dos textos que chegam do front, antes da validação
public final class Textos {

    private Textos() {
        // classe utilitária: não instanciável
    }

    // Tira espaços das pontas; texto vazio vira null (assim só o @NotBlank acusa o campo)
    public static String limpar(String valor) {
        if (valor == null) {
            return null;
        }
        String limpo = valor.trim();
        return limpo.isEmpty() ? null : limpo;
    }

    public static String emailNormalizado(String valor) {
        String limpo = limpar(valor);
        return limpo == null ? null : limpo.toLowerCase(Locale.ROOT);
    }

    // "11.222.333/0001-81" -> "11222333000181" (mantém letras para o CNPJ alfanumérico)
    public static String cnpjSemMascara(String valor) {
        if (valor == null) {
            return null;
        }
        String cnpj = valor.toUpperCase(Locale.ROOT).replaceAll("[^0-9A-Z]", "");
        return cnpj.isEmpty() ? null : cnpj;
    }

    // "(11) 91234-5678" -> "11912345678"
    public static String somenteDigitos(String valor) {
        if (valor == null) {
            return null;
        }
        String digitos = valor.replaceAll("\\D", "");
        return digitos.isEmpty() ? null : digitos;
    }
}
