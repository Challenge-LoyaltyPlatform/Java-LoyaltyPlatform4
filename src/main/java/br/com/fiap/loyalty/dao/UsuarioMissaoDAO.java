package br.com.fiap.loyalty.dao;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Consultas da TB_USUARIO_MISSAO (progresso dos usuários nas missões)
@Repository
public class UsuarioMissaoDAO {

    // Quantos registros de progresso existem nas missões de uma campanha (inclusive excluídas)
    public int contarParticipacoesNaCampanha(Connection conexao, int idCampanha) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                  FROM TB_USUARIO_MISSAO um
                  JOIN TB_MISSAO m ON m.id_missao = um.TB_MISSAO_id_missao
                 WHERE m.TB_CAMPANHA_id_campanha = ?
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idCampanha);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
