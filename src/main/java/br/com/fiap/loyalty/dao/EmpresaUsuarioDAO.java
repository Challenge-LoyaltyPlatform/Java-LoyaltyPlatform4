package br.com.fiap.loyalty.dao;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

// Consultas da TB_EMPRESA_USUARIO (vínculos entre usuários finais e empresas)
@Repository
public class EmpresaUsuarioDAO {

    public int contarUsuarios(Connection conexao, int idEmpresa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM TB_EMPRESA_USUARIO WHERE TB_EMPRESA_id_empresa = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // id da empresa -> quantidade de usuários vinculados (empresas sem usuários não aparecem)
    public Map<Integer, Integer> contarUsuariosPorEmpresa(Connection conexao) throws SQLException {
        String sql = """
                SELECT TB_EMPRESA_id_empresa, COUNT(*) AS total
                  FROM TB_EMPRESA_USUARIO
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
}
