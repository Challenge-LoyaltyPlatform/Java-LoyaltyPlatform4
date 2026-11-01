package br.com.fiap.loyalty.model;

import br.com.fiap.loyalty.exception.RegraNegocioException;

import java.time.LocalDate;

// Campanha de gamificação criada pela empresa cliente (TB_CAMPANHA)
public class Campanha implements Ativavel {

    //Definindo atributos da Campanha
    private Integer id;
    private Integer idEmpresa;
    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private StatusCampanha status;

    //Construtor padrão
    public Campanha() {
    }

    //Construtor com todos os parâmetros
    public Campanha(Integer id, Integer idEmpresa, String nome, String descricao,
                    LocalDate dataInicio, LocalDate dataFim, StatusCampanha status) {
        this.id = id;
        this.idEmpresa = idEmpresa;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
    }

    //Métodos Getters
    public Integer getId() {
        return id;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public StatusCampanha getStatus() {
        return status;
    }

    //Métodos Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public void setStatus(StatusCampanha status) {
        this.status = status;
    }

    //Métodos de negócio

    // RN23 — encerrar é terminal: campanha encerrada não volta a ser ativa
    @Override
    public void ativar() {
        if (this.status == StatusCampanha.ENCERRADA) {
            throw new RegraNegocioException("Campanha encerrada não pode ser reativada.");
        }
        if (this.status == StatusCampanha.ATIVA) {
            throw new RegraNegocioException("A campanha já está ativa.");
        }
        this.status = StatusCampanha.ATIVA;
    }

    @Override
    public void desativar() {
        if (this.status == StatusCampanha.ENCERRADA) {
            throw new RegraNegocioException("Campanha encerrada não pode ser inativada.");
        }
        if (this.status == StatusCampanha.INATIVA) {
            throw new RegraNegocioException("A campanha já está inativa.");
        }
        this.status = StatusCampanha.INATIVA;
    }

    // RN16 — a pontuação já conquistada permanece com os usuários após o encerramento
    public void encerrar() {
        if (this.status == StatusCampanha.ENCERRADA) {
            throw new RegraNegocioException("A campanha já está encerrada.");
        }
        this.status = StatusCampanha.ENCERRADA;
    }

    // RN09 — somente campanha ativa concede pontos e recompensas
    public boolean concedeRecompensa() {
        return this.status == StatusCampanha.ATIVA;
    }

    public boolean estaEncerrada() {
        return this.status == StatusCampanha.ENCERRADA;
    }

    // Campanha sem data de fim é contínua; com data, vale até o fim desse dia
    public boolean periodoTerminouAntesDe(LocalDate dia) {
        return this.dataFim != null && this.dataFim.isBefore(dia);
    }
}
