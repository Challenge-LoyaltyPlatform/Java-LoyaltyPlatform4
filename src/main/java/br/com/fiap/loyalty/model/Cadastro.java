package br.com.fiap.loyalty.model;

import java.time.LocalDate;

// Base das entidades com identidade própria, e-mail e data de cadastro (Empresa e Usuário)
public abstract class Cadastro {

    //Definindo atributos de Cadastro (abstrata)
    private Integer id;
    private String email;
    private LocalDate dataCadastro;

    //Construtor padrão
    public Cadastro() {
    }

    //Construtor parametrizado
    public Cadastro(Integer id, String email, LocalDate dataCadastro) {
        this.id = id;
        this.email = email;
        this.dataCadastro = dataCadastro;
    }

    //Métodos Getters
    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    //Métodos Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    //Método abstrato
    public abstract String getTipo();
}
