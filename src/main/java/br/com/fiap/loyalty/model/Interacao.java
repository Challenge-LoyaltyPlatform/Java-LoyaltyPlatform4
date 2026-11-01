package br.com.fiap.loyalty.model;

import java.time.LocalDateTime;

// Registro de uma interação do usuário com o app da empresa (TB_INTERACAO)
public class Interacao {

    //Definindo atributos da Interação (chave composta: usuário + empresa + data)
    private Integer idUsuario;
    private Integer idEmpresa;
    private LocalDateTime dataInteracao;
    private String tipo;

    //Construtor padrão
    public Interacao() {
    }

    //Construtor com todos os parâmetros
    public Interacao(Integer idUsuario, Integer idEmpresa, LocalDateTime dataInteracao, String tipo) {
        this.idUsuario = idUsuario;
        this.idEmpresa = idEmpresa;
        this.dataInteracao = dataInteracao;
        this.tipo = tipo;
    }

    //Métodos Getters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public LocalDateTime getDataInteracao() {
        return dataInteracao;
    }

    public String getTipo() {
        return tipo;
    }

    //Métodos Setters
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void setDataInteracao(LocalDateTime dataInteracao) {
        this.dataInteracao = dataInteracao;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
