package br.com.fiap.loyalty.model;

import br.com.fiap.loyalty.exception.RegraNegocioException;

// Missão de uma campanha: meta a cumprir e pontos de recompensa (TB_MISSAO)
public class Missao implements Ativavel {

    //Definindo atributos de Missao
    private Integer id;
    private Integer idCampanha;
    private String nome;
    private String descricao;
    private int meta;
    private int pontosRecompensa;
    private StatusMissao status;

    //Construtor padrão
    public Missao() {
    }

    //Construtor com todos os parâmetros
    public Missao(Integer id, Integer idCampanha, String nome, String descricao,
                  int meta, int pontosRecompensa, StatusMissao status) {
        this.id = id;
        this.idCampanha = idCampanha;
        this.nome = nome;
        this.descricao = descricao;
        this.meta = meta;
        this.pontosRecompensa = pontosRecompensa;
        this.status = status;
    }

    //Métodos Getters
    public Integer getId() {
        return id;
    }

    public Integer getIdCampanha() {
        return idCampanha;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getMeta() {
        return meta;
    }

    public int getPontosRecompensa() {
        return pontosRecompensa;
    }

    public StatusMissao getStatus() {
        return status;
    }

    //Métodos Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setIdCampanha(Integer idCampanha) {
        this.idCampanha = idCampanha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setMeta(int meta) {
        this.meta = meta;
    }

    public void setPontosRecompensa(int pontosRecompensa) {
        this.pontosRecompensa = pontosRecompensa;
    }

    public void setStatus(StatusMissao status) {
        this.status = status;
    }

    //Métodos de negócio

    // Verifica se o progresso informado atingiu a meta da missão
    public boolean verificarMetaAtingida(int progressoAtual) {
        return progressoAtual >= this.meta;
    }

    // RN24 — missão inativa ou excluída não concede pontos nem recompensas
    public boolean concedeRecompensa() {
        return this.status == StatusMissao.ATIVA;
    }

    public boolean estaAtiva() {
        return this.status == StatusMissao.ATIVA;
    }

    public boolean foiExcluida() {
        return this.status == StatusMissao.EXCLUIDA;
    }

    // RN25 — a exclusão é lógica e terminal: missão excluída não é reativada
    @Override
    public void ativar() {
        if (this.status == StatusMissao.EXCLUIDA) {
            throw new RegraNegocioException("Missão excluída não pode ser reativada.");
        }
        if (this.status == StatusMissao.ATIVA) {
            throw new RegraNegocioException("A missão já está ativa.");
        }
        this.status = StatusMissao.ATIVA;
    }

    @Override
    public void desativar() {
        if (this.status == StatusMissao.EXCLUIDA) {
            throw new RegraNegocioException("Missão excluída não pode ser inativada.");
        }
        if (this.status == StatusMissao.INATIVA) {
            throw new RegraNegocioException("A missão já está inativa.");
        }
        this.status = StatusMissao.INATIVA;
    }

    // RN25 — exclusão lógica: o histórico de conclusões é preservado
    public void excluir() {
        if (this.status == StatusMissao.EXCLUIDA) {
            throw new RegraNegocioException("A missão já foi excluída.");
        }
        this.status = StatusMissao.EXCLUIDA;
    }
}
