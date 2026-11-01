package br.com.fiap.loyalty.dto;

import java.time.LocalDate;

// Uma linha do ranking de usuários de uma empresa
public record ItemRankingResponse(
        int posicao,
        int idUsuario,
        String nomeUsuario,
        String nivel,
        int pontuacao,
        LocalDate atualizadoEm) {
}
