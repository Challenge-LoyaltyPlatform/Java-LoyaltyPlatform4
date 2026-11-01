package br.com.fiap.loyalty.model;

import java.time.LocalDate;

// Usuário final do app da empresa cliente (TB_USUARIO).
// Pontuação e nível ficam no vínculo com cada empresa (EmpresaUsuario)
public class Usuario extends Cadastro {

    //Definindo os atributos do Usuário
    private String nome;
    private String senhaHash;

    //Construtor padrão
    public Usuario() {
    }

    //Construtor com todos os parâmetros
    public Usuario(Integer id, String nome, String email, String senhaHash, LocalDate dataCadastro) {
        super(id, email, dataCadastro);
        this.nome = nome;
        this.senhaHash = senhaHash;
    }

    //Métodos Getters
    public String getNome() {
        return nome;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    //Métodos Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    @Override
    public String getTipo() {
        return "Usuário";
    }
}
