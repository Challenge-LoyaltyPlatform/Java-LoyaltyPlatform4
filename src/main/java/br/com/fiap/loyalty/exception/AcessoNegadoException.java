package br.com.fiap.loyalty.exception;

// Usuário logado, mas sem permissão para o recurso: vira 403 Forbidden
public class AcessoNegadoException extends RuntimeException {

    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }
}
