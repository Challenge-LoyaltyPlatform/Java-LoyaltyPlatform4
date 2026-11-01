package br.com.fiap.loyalty.model;

// Entidades que podem ser ativadas e desativadas (Empresa, Campanha, Missão, Sequência)
public interface Ativavel {

    void ativar();
    void desativar();
}
