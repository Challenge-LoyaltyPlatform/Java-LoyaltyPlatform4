package br.com.fiap.loyalty.dto;

import br.com.fiap.loyalty.model.Missao;
import br.com.fiap.loyalty.model.StatusMissao;

public record MissaoResponse(
        Integer id,
        Integer idCampanha,
        String nome,
        String descricao,
        int meta,
        int pontosRecompensa,
        StatusMissao status) {

    public static MissaoResponse de(Missao missao) {
        return new MissaoResponse(missao.getId(), missao.getIdCampanha(), missao.getNome(),
                missao.getDescricao(), missao.getMeta(), missao.getPontosRecompensa(), missao.getStatus());
    }
}
