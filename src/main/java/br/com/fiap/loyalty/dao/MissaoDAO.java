package br.com.fiap.loyalty.dao;

import br.com.fiap.loyalty.model.Missao;
import br.com.fiap.loyalty.model.StatusMissao;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// CRUD da TB_MISSAO. A exclusão pedida pela empresa é lógica (status EXCLUIDA)
@Repository
public class MissaoDAO {

    private static final String SELECT_MISSAO = """
            SELECT id_missao, TB_CAMPANHA_id_campanha, nome_missao, descricao_missao,
                   meta_missao, pontos_recompensa_missao, status_missao
              FROM TB_MISSAO
            """;

    // Total de missões (sem as excluídas) e quantas delas estão ativas
    public record Contagem(int total, int ativas) {
        public static final Contagem VAZIA = new Contagem(0, 0);
    }

    public int inserir(Connection conexao, Missao missao) throws SQLException {
        String sql = """
                INSERT INTO TB_MISSAO (nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao,
                                       status_missao, TB_CAMPANHA_id_campanha)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql, new String[]{"id_missao"})) {
            ps.setString(1, missao.getNome());
            JdbcUtils.definirTexto(ps, 2, missao.getDescricao());
            ps.setInt(3, missao.getMeta());
            ps.setInt(4, missao.getPontosRecompensa());
            ps.setString(5, missao.getStatus().name());
            ps.setInt(6, missao.getIdCampanha());
            ps.executeUpdate();
            return JdbcUtils.lerIdGerado(ps);
        }
    }

    public Optional<Missao> buscarPorId(Connection conexao, int id) throws SQLException {
        try (PreparedStatement ps = conexao.prepareStatement(SELECT_MISSAO + " WHERE id_missao = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(montar(rs)) : Optional.empty();
            }
        }
    }

    // Missões excluídas ficam no banco pelo histórico, mas não aparecem para a empresa
    public List<Missao> listarPorCampanha(Connection conexao, int idCampanha) throws SQLException {
        String sql = SELECT_MISSAO + " WHERE TB_CAMPANHA_id_campanha = ? AND status_missao <> 'EXCLUIDA' ORDER BY id_missao";
        List<Missao> missoes = new ArrayList<>();
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idCampanha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    missoes.add(montar(rs));
                }
            }
        }
        return missoes;
    }

    public boolean atualizar(Connection conexao, Missao missao) throws SQLException {
        String sql = """
                UPDATE TB_MISSAO
                   SET nome_missao = ?, descricao_missao = ?, meta_missao = ?, pontos_recompensa_missao = ?
                 WHERE id_missao = ?
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, missao.getNome());
            JdbcUtils.definirTexto(ps, 2, missao.getDescricao());
            ps.setInt(3, missao.getMeta());
            ps.setInt(4, missao.getPontosRecompensa());
            ps.setInt(5, missao.getId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean atualizarStatus(Connection conexao, int id, StatusMissao status) throws SQLException {
        String sql = "UPDATE TB_MISSAO SET status_missao = ? WHERE id_missao = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        }
    }

    // Exclusão física, usada só ao apagar o rascunho de uma campanha que nunca teve participantes
    public int excluirPorCampanha(Connection conexao, int idCampanha) throws SQLException {
        try (PreparedStatement ps = conexao.prepareStatement("DELETE FROM TB_MISSAO WHERE TB_CAMPANHA_id_campanha = ?")) {
            ps.setInt(1, idCampanha);
            return ps.executeUpdate();
        }
    }

    public Contagem contarPorCampanha(Connection conexao, int idCampanha) throws SQLException {
        String sql = """
                SELECT COUNT(*) AS total,
                       COUNT(CASE WHEN status_missao = 'ATIVA' THEN 1 END) AS ativas
                  FROM TB_MISSAO
                 WHERE TB_CAMPANHA_id_campanha = ? AND status_missao <> 'EXCLUIDA'
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idCampanha);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new Contagem(rs.getInt("total"), rs.getInt("ativas"));
            }
        }
    }

    // id da campanha -> contagem, para todas as campanhas de uma empresa de uma vez
    public Map<Integer, Contagem> contarPorCampanhasDaEmpresa(Connection conexao, int idEmpresa) throws SQLException {
        String sql = """
                SELECT m.TB_CAMPANHA_id_campanha AS id_campanha,
                       COUNT(*) AS total,
                       COUNT(CASE WHEN m.status_missao = 'ATIVA' THEN 1 END) AS ativas
                  FROM TB_MISSAO m
                  JOIN TB_CAMPANHA c ON c.id_campanha = m.TB_CAMPANHA_id_campanha
                 WHERE c.TB_EMPRESA_id_empresa = ? AND m.status_missao <> 'EXCLUIDA'
                 GROUP BY m.TB_CAMPANHA_id_campanha
                """;
        Map<Integer, Contagem> contagens = new HashMap<>();
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    contagens.put(rs.getInt("id_campanha"), new Contagem(rs.getInt("total"), rs.getInt("ativas")));
                }
            }
        }
        return contagens;
    }

    private Missao montar(ResultSet rs) throws SQLException {
        return new Missao(
                rs.getInt("id_missao"),
                rs.getInt("TB_CAMPANHA_id_campanha"),
                rs.getString("nome_missao"),
                rs.getString("descricao_missao"),
                rs.getInt("meta_missao"),
                rs.getInt("pontos_recompensa_missao"),
                StatusMissao.valueOf(rs.getString("status_missao")));
    }
}
