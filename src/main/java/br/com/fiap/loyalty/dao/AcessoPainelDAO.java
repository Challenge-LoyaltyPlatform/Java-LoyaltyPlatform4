package br.com.fiap.loyalty.dao;

import br.com.fiap.loyalty.model.AcessoPainel;
import br.com.fiap.loyalty.model.PerfilAcesso;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

// Consultas da TB_ACESSO_PAINEL (contas de login do painel web)
@Repository
public class AcessoPainelDAO {

    public Optional<AcessoPainel> buscarPorEmail(Connection conexao, String email) throws SQLException {
        String sql = """
                SELECT id_acesso, nome_acesso, email_acesso, senha_hash_acesso, perfil_acesso, TB_EMPRESA_id_empresa
                  FROM TB_ACESSO_PAINEL
                 WHERE LOWER(email_acesso) = LOWER(?)
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(montar(rs)) : Optional.empty();
            }
        }
    }

    private AcessoPainel montar(ResultSet rs) throws SQLException {
        // wasNull() vale para a última coluna lida, por isso é chamado logo depois do getInt
        int idEmpresa = rs.getInt("TB_EMPRESA_id_empresa");
        Integer idEmpresaOuNulo = rs.wasNull() ? null : idEmpresa;

        return new AcessoPainel(
                rs.getInt("id_acesso"),
                rs.getString("nome_acesso"),
                rs.getString("email_acesso"),
                rs.getString("senha_hash_acesso"),
                PerfilAcesso.valueOf(rs.getString("perfil_acesso")),
                idEmpresaOuNulo);
    }
}
