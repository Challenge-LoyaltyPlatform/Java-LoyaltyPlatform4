package br.com.fiap.loyalty.dto;

import br.com.fiap.loyalty.dao.MissaoDAO;
import br.com.fiap.loyalty.model.Campanha;
import br.com.fiap.loyalty.model.StatusCampanha;
import br.com.fiap.loyalty.model.StatusMissao;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;

// "missoes" só vem no detalhe (GET /campanhas/{id}); na listagem ficam só os totais
public record CampanhaResponse(
        Integer id,
        Integer idEmpresa,
        String nome,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim,
        StatusCampanha status,
        int totalMissoes,
        int missoesAtivas,
        @JsonInclude(JsonInclude.Include.NON_NULL) List<MissaoResponse> missoes) {

    public static CampanhaResponse resumo(Campanha campanha, MissaoDAO.Contagem contagem) {
        return new CampanhaResponse(campanha.getId(), campanha.getIdEmpresa(), campanha.getNome(),
                campanha.getDescricao(), campanha.getDataInicio(), campanha.getDataFim(), campanha.getStatus(),
                contagem.total(), contagem.ativas(), null);
    }

    public static CampanhaResponse detalhe(Campanha campanha, List<MissaoResponse> missoes) {
        int ativas = (int) missoes.stream().filter(missao -> missao.status() == StatusMissao.ATIVA).count();
        return new CampanhaResponse(campanha.getId(), campanha.getIdEmpresa(), campanha.getNome(),
                campanha.getDescricao(), campanha.getDataInicio(), campanha.getDataFim(), campanha.getStatus(),
                missoes.size(), ativas, missoes);
    }
}
