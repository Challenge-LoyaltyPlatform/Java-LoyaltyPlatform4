package br.com.fiap.loyalty.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Vínculo usuário-empresa (TB_EMPRESA_USUARIO): aqui moram a pontuação e o nível, por empresa
public class EmpresaUsuario {

    // Mínimo de dias de vínculo para participar do ranking (RN10)
    private static final int DIAS_MINIMOS_RANKING = 3;

    //Definindo atributos do vínculo entre usuário e empresa (chave composta)
    private Integer idEmpresa;
    private Integer idUsuario;
    private Integer idNivel;
    private int pontuacaoAcumulada;
    private LocalDate dataAssociacao;

    //Construtor padrão
    public EmpresaUsuario() {
    }

    //Construtor com todos os parâmetros
    public EmpresaUsuario(Integer idEmpresa, Integer idUsuario, Integer idNivel,
                          int pontuacaoAcumulada, LocalDate dataAssociacao) {
        this.idEmpresa = idEmpresa;
        this.idUsuario = idUsuario;
        this.idNivel = idNivel;
        this.pontuacaoAcumulada = pontuacaoAcumulada;
        this.dataAssociacao = dataAssociacao;
    }

    //Métodos Getters
    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public Integer getIdNivel() {
        return idNivel;
    }

    public int getPontuacaoAcumulada() {
        return pontuacaoAcumulada;
    }

    public LocalDate getDataAssociacao() {
        return dataAssociacao;
    }

    //Métodos Setters
    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdNivel(Integer idNivel) {
        this.idNivel = idNivel;
    }

    public void setPontuacaoAcumulada(int pontuacaoAcumulada) {
        this.pontuacaoAcumulada = pontuacaoAcumulada;
    }

    public void setDataAssociacao(LocalDate dataAssociacao) {
        this.dataAssociacao = dataAssociacao;
    }

    //Métodos de negócio

    // RN06 — a pontuação de missão deve ser um inteiro positivo
    public void acumularPontos(int pontos) {
        if (pontos <= 0) {
            throw new IllegalArgumentException("Pontuação deve ser um inteiro positivo. Recebido: " + pontos);
        }
        this.pontuacaoAcumulada += pontos;
    }

    // RN08/RN17 — o nível é definido pela pontuação acumulada no vínculo
    public boolean atualizarNivel(Nivel nivel) {
        if (nivel.contemPontuacao(this.pontuacaoAcumulada)) {
            this.idNivel = nivel.getId();
            return true;
        }
        return false;
    }

    // RN18 — progresso percentual dentro da faixa do nível atual
    public int calcularProgressoNoNivel(Nivel nivel) {
        int faixa = nivel.getPontosMaximos() - nivel.getPontosMinimos();
        if (faixa <= 0) {
            return 100;
        }
        int avanco = this.pontuacaoAcumulada - nivel.getPontosMinimos();
        if (avanco <= 0) {
            return 0;
        }
        return Math.min(100, (avanco * 100) / faixa);
    }

    // RN10 — somente vínculos com 3 dias ou mais participam do ranking
    public boolean podeParticiparDoRanking(LocalDate dataReferencia) {
        if (this.dataAssociacao == null || dataReferencia == null) {
            return false;
        }
        return ChronoUnit.DAYS.between(this.dataAssociacao, dataReferencia) >= DIAS_MINIMOS_RANKING;
    }
}
