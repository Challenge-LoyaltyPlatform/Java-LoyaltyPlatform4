package br.com.fiap.loyalty.model;

// Faixa de classificação por pontuação: Bronze, Prata, Ouro... (TB_NIVEL)
public class Nivel {

    //Definindo atributos de Nivel
    private Integer id;
    private String nome;
    private int pontosMinimos;
    private int pontosMaximos;

    //Construtor vazio
    public Nivel() {
    }

    //Construtor com todos os parâmetros
    public Nivel(Integer id, String nome, int pontosMinimos, int pontosMaximos) {
        this.id = id;
        this.nome = nome;
        this.pontosMinimos = pontosMinimos;
        this.pontosMaximos = pontosMaximos;
    }

    //Métodos Getters
    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getPontosMinimos() {
        return pontosMinimos;
    }

    public int getPontosMaximos() {
        return pontosMaximos;
    }

    //Métodos Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPontosMinimos(int pontosMinimos) {
        this.pontosMinimos = pontosMinimos;
    }

    public void setPontosMaximos(int pontosMaximos) {
        this.pontosMaximos = pontosMaximos;
    }

    //Método de negócio — o nível sabe qual faixa de pontuação ele cobre
    public boolean contemPontuacao(int pontuacao) {
        return pontuacao >= this.pontosMinimos && pontuacao <= this.pontosMaximos;
    }
}
