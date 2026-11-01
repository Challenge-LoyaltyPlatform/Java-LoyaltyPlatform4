package br.com.fiap.loyalty.model;

import br.com.fiap.loyalty.exception.RegraNegocioException;

import java.time.LocalDate;

// Progresso de um usuário em uma missão (TB_USUARIO_MISSAO, chave composta usuário + missão)
public class UsuarioMissao {

    //Definindo atributos do progresso
    private Integer idUsuario;
    private Integer idMissao;
    private int progressoAtual;
    private LocalDate dataInicio;
    private LocalDate dataConclusao;
    private LocalDate dataResgate;

    //Construtor padrão
    public UsuarioMissao() {
    }

    //Construtor com todos os parâmetros
    public UsuarioMissao(Integer idUsuario, Integer idMissao, int progressoAtual, LocalDate dataInicio,
                         LocalDate dataConclusao, LocalDate dataResgate) {
        this.idUsuario = idUsuario;
        this.idMissao = idMissao;
        this.progressoAtual = progressoAtual;
        this.dataInicio = dataInicio;
        this.dataConclusao = dataConclusao;
        this.dataResgate = dataResgate;
    }

    //Métodos Getters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public Integer getIdMissao() {
        return idMissao;
    }

    public int getProgressoAtual() {
        return progressoAtual;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public LocalDate getDataResgate() {
        return dataResgate;
    }

    //Métodos Setters
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdMissao(Integer idMissao) {
        this.idMissao = idMissao;
    }

    public void setProgressoAtual(int progressoAtual) {
        this.progressoAtual = progressoAtual;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public void setDataResgate(LocalDate dataResgate) {
        this.dataResgate = dataResgate;
    }

    //Métodos de negócio

    public boolean estaConcluida() {
        return this.dataConclusao != null;
    }

    public boolean foiResgatada() {
        return this.dataResgate != null;
    }

    // Soma o avanço e marca a conclusão quando a meta da missão é atingida
    public void registrarProgresso(int avanco, Missao missao, LocalDate dia) {
        if (avanco <= 0) {
            throw new IllegalArgumentException("O avanço deve ser um inteiro positivo. Recebido: " + avanco);
        }
        if (!missao.concedeRecompensa()) {
            throw new RegraNegocioException("A missão não está ativa.");
        }
        this.progressoAtual += avanco;
        if (this.dataConclusao == null && missao.verificarMetaAtingida(this.progressoAtual)) {
            this.dataConclusao = dia;
        }
    }

    // A recompensa só pode ser resgatada uma vez, depois de concluída
    public void resgatar(LocalDate dia) {
        if (!estaConcluida()) {
            throw new RegraNegocioException("A missão ainda não foi concluída.");
        }
        if (foiResgatada()) {
            throw new RegraNegocioException("A recompensa desta missão já foi resgatada.");
        }
        this.dataResgate = dia;
    }
}
