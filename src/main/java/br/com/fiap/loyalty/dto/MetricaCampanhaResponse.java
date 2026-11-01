package br.com.fiap.loyalty.dto;

import br.com.fiap.loyalty.model.StatusCampanha;
import br.com.fiap.loyalty.util.Numeros;

// Participação e resultado de uma campanha (missões excluídas não entram na conta)
public record MetricaCampanhaResponse(
        int idCampanha,
        String nome,
        StatusCampanha status,
        int missoes,
        int participantes,
        int missoesIniciadas,
        int missoesConcluidas,
        double taxaConclusaoPct,
        long pontosDistribuidos) {

    public static MetricaCampanhaResponse de(int idCampanha, String nome, StatusCampanha status, int missoes,
                                             int participantes, int missoesIniciadas, int missoesConcluidas,
                                             long pontosDistribuidos) {
        return new MetricaCampanhaResponse(idCampanha, nome, status, missoes, participantes, missoesIniciadas,
                missoesConcluidas, Numeros.percentual(missoesConcluidas, missoesIniciadas), pontosDistribuidos);
    }
}
