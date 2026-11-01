package br.com.fiap.loyalty.model;

import java.time.LocalDate;

// Classificação do usuário dentro de uma empresa (TB_RANKING, chave composta usuário + empresa)
public class Ranking {

    //Definindo atributos do Ranking
    private Integer idUsuario;
    private Integer idEmpresa;
    private int posicao;
    private int pontuacao;
    private LocalDate dataAtualizacao;

    //Construtor padrão
    public Ranking() {
    }

    //Construtor com todos os parâmetros
    public Ranking(Integer idUsuario, Integer idEmpresa, int posicao, int pontuacao, LocalDate dataAtualizacao) {
        this.idUsuario = idUsuario;
        this.idEmpresa = idEmpresa;
        this.posicao = posicao;
        this.pontuacao = pontuacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    //Métodos Getters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public int getPosicao() {
        return posicao;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public LocalDate getDataAtualizacao() {
        return dataAtualizacao;
    }

    //Métodos Setters
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public void setDataAtualizacao(LocalDate dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    //Método de negócio — a posição no ranking começa em 1
    public void atualizarPosicao(int novaPosicao, LocalDate dataAtualizacao) {
        if (novaPosicao < 1) {
            throw new IllegalArgumentException("Posição no ranking deve ser maior ou igual a 1. Recebido: " + novaPosicao);
        }
        this.posicao = novaPosicao;
        this.dataAtualizacao = dataAtualizacao;
    }
}
