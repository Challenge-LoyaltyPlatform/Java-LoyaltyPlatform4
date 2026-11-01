package br.com.fiap.loyalty.dao;

import br.com.fiap.loyalty.dto.ItemRankingResponse;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Leitura da TB_RANKING (consulta 1 da entrega de banco: ranking de usuários da empresa)
@Repository
public class RankingDAO {

    public List<ItemRankingResponse> listarPorEmpresa(Connection conexao, int idEmpresa) throws SQLException {
        String sql = """
                SELECT r.posicao_ranking, u.id_usuario, u.nome_usuario, n.nome_nivel,
                       r.pontuacao_ranking, r.data_atualizacao_ranking
                  FROM TB_RANKING r
                  JOIN TB_USUARIO u
                    ON u.id_usuario = r.TB_USUARIO_id_usuario
                  JOIN TB_EMPRESA_USUARIO eu
                    ON eu.TB_USUARIO_id_usuario = r.TB_USUARIO_id_usuario
                   AND eu.TB_EMPRESA_id_empresa = r.TB_EMPRESA_id_empresa
                  JOIN TB_NIVEL n
                    ON n.id_nivel = eu.TB_NIVEL_id_nivel
                 WHERE r.TB_EMPRESA_id_empresa = ?
                 ORDER BY r.posicao_ranking
                """;
        List<ItemRankingResponse> ranking = new ArrayList<>();
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ranking.add(new ItemRankingResponse(
                            rs.getInt("posicao_ranking"),
                            rs.getInt("id_usuario"),
                            rs.getString("nome_usuario"),
                            rs.getString("nome_nivel"),
                            rs.getInt("pontuacao_ranking"),
                            JdbcUtils.lerData(rs, "data_atualizacao_ranking")));
                }
            }
        }
        return ranking;
    }
}
