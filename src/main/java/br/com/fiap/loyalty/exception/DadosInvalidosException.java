package br.com.fiap.loyalty.exception;

import java.util.Map;

// Validação que o Bean Validation não cobre sozinho (ex.: data de fim antes da de início): vira 400
public class DadosInvalidosException extends RuntimeException {

    private final Map<String, String> campos;

    public DadosInvalidosException(String mensagem, Map<String, String> campos) {
        super(mensagem);
        this.campos = Map.copyOf(campos);
    }

    public Map<String, String> getCampos() {
        return campos;
    }
}
