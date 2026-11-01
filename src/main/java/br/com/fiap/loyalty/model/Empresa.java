package br.com.fiap.loyalty.model;

import br.com.fiap.loyalty.exception.RegraNegocioException;

import java.time.LocalDate;

// Empresa cliente que contrata a plataforma (TB_EMPRESA)
public class Empresa extends Cadastro implements Ativavel {

    //Definindo atributos de Empresa
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String telefone;
    private IdentidadeVisual identidadeVisual;
    private StatusEmpresa status;

    //Construtor vazio
    public Empresa() {
    }

    //Construtor com todos os parâmetros
    public Empresa(Integer id, String razaoSocial, String nomeFantasia, String cnpj, String email,
                   String telefone, IdentidadeVisual identidadeVisual, StatusEmpresa status,
                   LocalDate dataCadastro) {
        super(id, email, dataCadastro);
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.identidadeVisual = identidadeVisual;
        this.status = status;
    }

    //Métodos Getters
    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public IdentidadeVisual getIdentidadeVisual() {
        return identidadeVisual;
    }

    public StatusEmpresa getStatus() {
        return status;
    }

    //Métodos Setters
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setIdentidadeVisual(IdentidadeVisual identidadeVisual) {
        this.identidadeVisual = identidadeVisual;
    }

    public void setStatus(StatusEmpresa status) {
        this.status = status;
    }

    @Override
    public String getTipo() {
        return "Empresa";
    }

    //Métodos de negócio

    // RN21 — somente o Administrador da Loyalty ativa e inativa empresas (checado no BO)
    @Override
    public void ativar() {
        if (this.status == StatusEmpresa.ATIVA) {
            throw new RegraNegocioException("A empresa já está ativa.");
        }
        this.status = StatusEmpresa.ATIVA;
    }

    @Override
    public void desativar() {
        if (this.status == StatusEmpresa.INATIVA) {
            throw new RegraNegocioException("A empresa já está inativa.");
        }
        this.status = StatusEmpresa.INATIVA;
    }

    public boolean estaAtiva() {
        return this.status == StatusEmpresa.ATIVA;
    }
}
