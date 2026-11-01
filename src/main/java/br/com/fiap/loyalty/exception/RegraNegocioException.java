package br.com.fiap.loyalty.exception;

import java.util.Map;

// Violação de regra de negócio ou de unicidade: vira 409 Conflict
public class RegraNegocioException extends RuntimeException {

    // Mensagem por campo do formulário, quando o erro é de um campo específico
    private final Map<String, String> campos;

    public RegraNegocioException(String mensagem) {
        this(mensagem, Map.of());
    }

    public RegraNegocioException(String mensagem, Map<String, String> campos) {
        super(mensagem);
        this.campos = Map.copyOf(campos);
    }

    public Map<String, String> getCampos() {
        return campos;
    }
}
