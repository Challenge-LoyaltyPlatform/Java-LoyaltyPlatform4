package br.com.fiap.loyalty.dao;

import br.com.fiap.loyalty.dto.MetricaCampanhaResponse;
import br.com.fiap.loyalty.model.StatusCampanha;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Relatórios de engajamento (consultas 4 e 6 da entrega de banco, filtradas por empresa)
@Repository
public class MetricasDAO {

    public record Engajamento(int usuariosVinculados, long pontosTotais, double mediaPontos, int maiorPontuacao,
                              int sequenciasAtivas, int sequenciasEmRisco) {
    }

    // LEFT JOIN a partir da empresa: empresa sem usuários devolve zeros em vez de nenhuma linha
    public Engajamento buscarEngajamento(Connection conexao, int idEmpresa) throws SQLException {
        String sql = """
                SELECT COUNT(eu.TB_USUARIO_id_usuario)                              AS usuarios_vinculados,
                       NVL(SUM(eu.pontuacao_acumulada), 0)                           AS pontos_totais,
                       NVL(ROUND(AVG(eu.pontuacao_acumulada), 1), 0)                 AS media_pontos,
                       NVL(MAX(eu.pontuacao_acumulada), 0)                           AS maior_pontuacao,
                       COUNT(CASE WHEN s.status_sequencia = 'ATIVA' THEN 1 END)      AS sequencias_ativas,
                       COUNT(CASE WHEN s.status_sequencia = 'SUSPENSA' THEN 1 END)   AS sequencias_em_risco
                  FROM TB_EMPRESA e
                  LEFT JOIN TB_EMPRESA_USUARIO eu
                    ON eu.TB_EMPRESA_id_empresa = e.id_empresa
                  LEFT JOIN TB_SEQUENCIA s
                    ON s.TB_EMPRESA_id_empresa = eu.TB_EMPRESA_id_empresa
                   AND s.TB_USUARIO_id_usuario = eu.TB_USUARIO_id_usuario
                 WHERE e.id_empresa = ?
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new Engajamento(
                        rs.getInt("usuarios_vinculados"),
                        rs.getLong("pontos_totais"),
                        rs.getDouble("media_pontos"),
                        rs.getInt("maior_pontuacao"),
                        rs.getInt("sequencias_ativas"),
                        rs.getInt("sequencias_em_risco"));
            }
        }
    }

    public List<MetricaCampanhaResponse> listarPorCampanha(Connection conexao, int idEmpresa) throws SQLException {
        String sql = """
                SELECT c.id_campanha, c.nome_campanha, c.status_campanha,
                       COUNT(DISTINCT m.id_missao)                AS missoes,
                       COUNT(DISTINCT um.TB_USUARIO_id_usuario)   AS participantes,
                       COUNT(um.TB_MISSAO_id_missao)              AS missoes_iniciadas,
                       COUNT(um.data_conclusao)                   AS missoes_concluidas,
                       NVL(SUM(CASE WHEN um.data_resgate IS NOT NULL
                                    THEN m.pontos_recompensa_missao END), 0) AS pontos_distribuidos
                  FROM TB_CAMPANHA c
                  LEFT JOIN TB_MISSAO m
                    ON m.TB_CAMPANHA_id_campanha = c.id_campanha
                   AND m.status_missao <> 'EXCLUIDA'
                  LEFT JOIN TB_USUARIO_MISSAO um
                    ON um.TB_MISSAO_id_missao = m.id_missao
                 WHERE c.TB_EMPRESA_id_empresa = ?
                 GROUP BY c.id_campanha, c.nome_campanha, c.status_campanha
                 ORDER BY pontos_distribuidos DESC, c.id_campanha
                """;
        List<MetricaCampanhaResponse> metricas = new ArrayList<>();
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    metricas.add(MetricaCampanhaResponse.de(
                            rs.getInt("id_campanha"),
                            rs.getString("nome_campanha"),
                            StatusCampanha.valueOf(rs.getString("status_campanha")),
                            rs.getInt("missoes"),
                            rs.getInt("participantes"),
                            rs.getInt("missoes_iniciadas"),
                            rs.getInt("missoes_concluidas"),
                            rs.getLong("pontos_distribuidos")));
                }
            }
        }
        return metricas;
    }
}
