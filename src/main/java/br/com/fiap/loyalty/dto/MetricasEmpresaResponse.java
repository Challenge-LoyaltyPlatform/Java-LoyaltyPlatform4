package br.com.fiap.loyalty.dto;

import java.util.List;

// Engajamento geral dos usuários de uma empresa + métricas de cada campanha dela
public record MetricasEmpresaResponse(
        int idEmpresa,
        int usuariosVinculados,
        long pontosTotais,
        double mediaPontos,
        int maiorPontuacao,
        int sequenciasAtivas,
        int sequenciasEmRisco,
        double usuariosAtivosPct,
        List<MetricaCampanhaResponse> campanhas) {
}
