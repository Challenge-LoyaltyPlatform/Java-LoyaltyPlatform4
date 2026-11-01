package br.com.fiap.loyalty.model;

// Conta de quem acessa o painel web (TB_ACESSO_PAINEL).
// Não confundir com Usuario, que é o usuário final do app da empresa cliente
public class AcessoPainel {

    //Definindo atributos do Acesso ao Painel
    private Integer id;
    private String nome;
    private String email;
    private String senhaHash;
    private PerfilAcesso perfil;
    private Integer idEmpresa; // null para o perfil ADMIN

    //Construtor padrão
    public AcessoPainel() {
    }

    //Construtor com todos os parâmetros
    public AcessoPainel(Integer id, String nome, String email, String senhaHash,
                        PerfilAcesso perfil, Integer idEmpresa) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
        this.idEmpresa = idEmpresa;
    }

    //Métodos Getters
    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public PerfilAcesso getPerfil() {
        return perfil;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    //Métodos Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public void setPerfil(PerfilAcesso perfil) {
        this.perfil = perfil;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    //Método de negócio
    public boolean isAdmin() {
        return this.perfil == PerfilAcesso.ADMIN;
    }
}
