package br.com.fiap.loyalty.dao;

import br.com.fiap.loyalty.model.Campanha;
import br.com.fiap.loyalty.model.StatusCampanha;
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

// CRUD da TB_CAMPANHA
@Repository
public class CampanhaDAO {

    private static final String SELECT_CAMPANHA = """
            SELECT id_campanha, TB_EMPRESA_id_empresa, nome_campanha, descricao_campanha,
                   data_inicio_campanha, data_fim_campanha, status_campanha
              FROM TB_CAMPANHA
            """;

    public int inserir(Connection conexao, Campanha campanha) throws SQLException {
        String sql = """
                INSERT INTO TB_CAMPANHA (nome_campanha, descricao_campanha, data_inicio_campanha,
                                         data_fim_campanha, status_campanha, TB_EMPRESA_id_empresa)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql, new String[]{"id_campanha"})) {
            ps.setString(1, campanha.getNome());
            JdbcUtils.definirTexto(ps, 2, campanha.getDescricao());
            JdbcUtils.definirData(ps, 3, campanha.getDataInicio());
            JdbcUtils.definirData(ps, 4, campanha.getDataFim());
            ps.setString(5, campanha.getStatus().name());
            ps.setInt(6, campanha.getIdEmpresa());
            ps.executeUpdate();
            return JdbcUtils.lerIdGerado(ps);
        }
    }

    public Optional<Campanha> buscarPorId(Connection conexao, int id) throws SQLException {
        try (PreparedStatement ps = conexao.prepareStatement(SELECT_CAMPANHA + " WHERE id_campanha = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(montar(rs)) : Optional.empty();
            }
        }
    }

    // Mais recentes primeiro
    public List<Campanha> listarPorEmpresa(Connection conexao, int idEmpresa) throws SQLException {
        String sql = SELECT_CAMPANHA + " WHERE TB_EMPRESA_id_empresa = ? ORDER BY data_inicio_campanha DESC, id_campanha DESC";
        List<Campanha> campanhas = new ArrayList<>();
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    campanhas.add(montar(rs));
                }
            }
        }
        return campanhas;
    }

    public boolean atualizar(Connection conexao, Campanha campanha) throws SQLException {
        String sql = """
                UPDATE TB_CAMPANHA
                   SET nome_campanha = ?, descricao_campanha = ?, data_inicio_campanha = ?, data_fim_campanha = ?
                 WHERE id_campanha = ?
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, campanha.getNome());
            JdbcUtils.definirTexto(ps, 2, campanha.getDescricao());
            JdbcUtils.definirData(ps, 3, campanha.getDataInicio());
            JdbcUtils.definirData(ps, 4, campanha.getDataFim());
            ps.setInt(5, campanha.getId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean atualizarStatus(Connection conexao, int id, StatusCampanha status) throws SQLException {
        String sql = "UPDATE TB_CAMPANHA SET status_campanha = ? WHERE id_campanha = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean excluir(Connection conexao, int id) throws SQLException {
        try (PreparedStatement ps = conexao.prepareStatement("DELETE FROM TB_CAMPANHA WHERE id_campanha = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    // Inativar a empresa encerra as campanhas ativas dela; devolve quantas foram encerradas
    public int encerrarAtivasDaEmpresa(Connection conexao, int idEmpresa) throws SQLException {
        String sql = """
                UPDATE TB_CAMPANHA
                   SET status_campanha = 'ENCERRADA'
                 WHERE TB_EMPRESA_id_empresa = ? AND status_campanha = 'ATIVA'
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            return ps.executeUpdate();
        }
    }

    public int contarAtivas(Connection conexao, int idEmpresa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM TB_CAMPANHA WHERE TB_EMPRESA_id_empresa = ? AND status_campanha = 'ATIVA'";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // id da empresa -> quantidade de campanhas ativas (empresas sem campanha ativa não aparecem)
    public Map<Integer, Integer> contarAtivasPorEmpresa(Connection conexao) throws SQLException {
        String sql = """
                SELECT TB_EMPRESA_id_empresa, COUNT(*) AS total
                  FROM TB_CAMPANHA
                 WHERE status_campanha = 'ATIVA'
                 GROUP BY TB_EMPRESA_id_empresa
                """;
        Map<Integer, Integer> totais = new HashMap<>();
        try (PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                totais.put(rs.getInt("TB_EMPRESA_id_empresa"), rs.getInt("total"));
            }
        }
        return totais;
    }

    private Campanha montar(ResultSet rs) throws SQLException {
        return new Campanha(
                rs.getInt("id_campanha"),
                rs.getInt("TB_EMPRESA_id_empresa"),
                rs.getString("nome_campanha"),
                rs.getString("descricao_campanha"),
                JdbcUtils.lerData(rs, "data_inicio_campanha"),
                JdbcUtils.lerData(rs, "data_fim_campanha"),
                StatusCampanha.valueOf(rs.getString("status_campanha")));
    }
}
