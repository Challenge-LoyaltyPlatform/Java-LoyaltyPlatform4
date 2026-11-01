package br.com.fiap.loyalty.exception;

// Registro procurado não existe: vira 404 Not Found
public class NaoEncontradoException extends RuntimeException {

    public NaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
