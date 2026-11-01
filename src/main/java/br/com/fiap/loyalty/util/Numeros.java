package br.com.fiap.loyalty.util;

public final class Numeros {

    private Numeros() {
        // classe utilitária: não instanciável
    }

    // Percentual com uma casa decimal; sem base de cálculo, devolve 0
    public static double percentual(long parte, long total) {
        if (total <= 0) {
            return 0;
        }
        return Math.round(parte * 1000.0 / total) / 10.0;
    }
}
