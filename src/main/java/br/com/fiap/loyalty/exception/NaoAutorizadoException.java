package br.com.fiap.loyalty.exception;

// Login inválido ou token ausente/expirado: vira 401 Unauthorized
public class NaoAutorizadoException extends RuntimeException {

    public NaoAutorizadoException(String mensagem) {
        super(mensagem);
    }
}
