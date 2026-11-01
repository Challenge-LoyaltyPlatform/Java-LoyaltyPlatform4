package br.com.fiap.loyalty.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// Dias consecutivos de interação do usuário, por empresa (TB_SEQUENCIA, chave composta usuário + empresa)
public class Sequencia implements Ativavel {

    // Janelas da sequência de dias (RN13 e RN14)
    private static final int HORAS_PARA_SUSPENDER = 24;
    private static final int HORAS_PARA_ZERAR = 48;

    //Definindo atributos de Sequencia
    private Integer idUsuario;
    private Integer idEmpresa;
    private int diasConsecutivos;
    private StatusSequencia status;
    private LocalDate dataInicio;
    private LocalDateTime ultimoAcesso;
    private LocalDate dataDesativacao;

    //Construtor padrão
    public Sequencia() {
    }

    //Construtor com todos os parâmetros
    public Sequencia(Integer idUsuario, Integer idEmpresa, int diasConsecutivos, StatusSequencia status,
                     LocalDate dataInicio, LocalDateTime ultimoAcesso, LocalDate dataDesativacao) {
        this.idUsuario = idUsuario;
        this.idEmpresa = idEmpresa;
        this.diasConsecutivos = diasConsecutivos;
        this.status = status;
        this.dataInicio = dataInicio;
        this.ultimoAcesso = ultimoAcesso;
        this.dataDesativacao = dataDesativacao;
    }

    //Métodos Getters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public int getDiasConsecutivos() {
        return diasConsecutivos;
    }

    public StatusSequencia getStatus() {
        return status;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    public LocalDate getDataDesativacao() {
        return dataDesativacao;
    }

    //Métodos Setters
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void setDiasConsecutivos(int diasConsecutivos) {
        this.diasConsecutivos = diasConsecutivos;
    }

    public void setStatus(StatusSequencia status) {
        this.status = status;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public void setDataDesativacao(LocalDate dataDesativacao) {
        this.dataDesativacao = dataDesativacao;
    }

    //Métodos de negócio

    // RN12 — a sequência incrementa 1 por DIA de interação, não por interação.
    // RN13 — a próxima interação dentro da janela restaura a sequência automaticamente.
    // Devolve true se o dia foi contado, false se o usuário já havia interagido hoje.
    public boolean registrarAcesso(LocalDateTime dataHoraAcesso) {
        if (dataHoraAcesso == null) {
            throw new IllegalArgumentException("A data e hora do acesso não podem ser nulas.");
        }

        LocalDate diaDoAcesso = dataHoraAcesso.toLocalDate();

        // Já contou hoje: registra a interação mas não incrementa o contador
        if (this.ultimoAcesso != null && this.ultimoAcesso.toLocalDate().isEqual(diaDoAcesso)) {
            this.ultimoAcesso = dataHoraAcesso;
            return false;
        }

        if (this.diasConsecutivos == 0 || this.dataInicio == null) {
            this.dataInicio = diaDoAcesso;
        }
        this.status = StatusSequencia.ATIVA;
        this.dataDesativacao = null;
        this.diasConsecutivos++;
        this.ultimoAcesso = dataHoraAcesso;
        return true;
    }

    // RN13 e RN14 — avalia quanto tempo passou desde o último acesso e aplica a janela
    public void avaliarJanela(LocalDateTime dataHoraReferencia) {
        if (this.ultimoAcesso == null || this.status == StatusSequencia.ZERADA) {
            return;
        }

        long horas = ChronoUnit.HOURS.between(this.ultimoAcesso, dataHoraReferencia);
        if (horas >= HORAS_PARA_ZERAR) {
            this.dataDesativacao = dataHoraReferencia.toLocalDate();
            zerar();
        } else if (horas >= HORAS_PARA_SUSPENDER) {
            suspender(dataHoraReferencia.toLocalDate());
        }
    }

    // RN13 — passou de 24h sem interação, mas a sequência ainda é restaurável
    public void suspender(LocalDate dataDesativacao) {
        this.status = StatusSequencia.SUSPENSA;
        this.dataDesativacao = dataDesativacao;
    }

    // RN14 — passou de 48h sem interação, a contagem reinicia
    public void zerar() {
        this.status = StatusSequencia.ZERADA;
        this.diasConsecutivos = 0;
    }

    public boolean estaAtiva() {
        return this.status == StatusSequencia.ATIVA;
    }

    @Override
    public void ativar() {
        if (this.status == StatusSequencia.ATIVA) {
            throw new IllegalStateException("A sequência já está ativa.");
        }
        this.status = StatusSequencia.ATIVA;
        this.dataDesativacao = null;
    }

    // Encerrar a sequência equivale a zerá-la
    @Override
    public void desativar() {
        zerar();
    }
}
