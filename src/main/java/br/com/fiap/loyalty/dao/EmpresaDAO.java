package br.com.fiap.loyalty.dao;

import br.com.fiap.loyalty.model.Empresa;
import br.com.fiap.loyalty.model.FormaBotoes;
import br.com.fiap.loyalty.model.IdentidadeVisual;
import br.com.fiap.loyalty.model.StatusEmpresa;
import br.com.fiap.loyalty.model.Tipografia;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// CRUD da TB_EMPRESA. Empresa não é excluída (decisão da Sprint 3): ela é inativada
@Repository
public class EmpresaDAO {

    private static final String SELECT_EMPRESA = """
            SELECT id_empresa, razao_social_empresa, nome_empresa, cnpj_empresa, email_empresa,
                   telefone_empresa, cor_principal_empresa, tipografia_empresa, forma_botoes_empresa,
                   status_empresa, data_cadastro_empresa
              FROM TB_EMPRESA
            """;

    public int inserir(Connection conexao, Empresa empresa) throws SQLException {
        String sql = """
                INSERT INTO TB_EMPRESA (razao_social_empresa, nome_empresa, cnpj_empresa, email_empresa,
                                        telefone_empresa, cor_principal_empresa, tipografia_empresa,
                                        forma_botoes_empresa, status_empresa, data_cadastro_empresa)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql, new String[]{"id_empresa"})) {
            int indice = preencherDadosCadastrais(ps, empresa);
            ps.setString(indice++, empresa.getStatus().name());
            JdbcUtils.definirData(ps, indice, empresa.getDataCadastro());
            ps.executeUpdate();
            return JdbcUtils.lerIdGerado(ps);
        }
    }

    public Optional<Empresa> buscarPorId(Connection conexao, int id) throws SQLException {
        return buscarUma(conexao, SELECT_EMPRESA + " WHERE id_empresa = ?", id);
    }

    public Optional<Empresa> buscarPorCnpj(Connection conexao, String cnpj) throws SQLException {
        return buscarUma(conexao, SELECT_EMPRESA + " WHERE cnpj_empresa = ?", cnpj);
    }

    public Optional<Empresa> buscarPorEmail(Connection conexao, String email) throws SQLException {
        return buscarUma(conexao, SELECT_EMPRESA + " WHERE LOWER(email_empresa) = LOWER(?)", email);
    }

    public List<Empresa> listar(Connection conexao) throws SQLException {
        List<Empresa> empresas = new ArrayList<>();
        try (PreparedStatement ps = conexao.prepareStatement(SELECT_EMPRESA + " ORDER BY id_empresa");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                empresas.add(montar(rs));
            }
        }
        return empresas;
    }

    // Atualiza dados cadastrais e identidade visual; status e data de cadastro não mudam aqui
    public boolean atualizar(Connection conexao, Empresa empresa) throws SQLException {
        String sql = """
                UPDATE TB_EMPRESA
                   SET razao_social_empresa = ?, nome_empresa = ?, cnpj_empresa = ?, email_empresa = ?,
                       telefone_empresa = ?, cor_principal_empresa = ?, tipografia_empresa = ?,
                       forma_botoes_empresa = ?
                 WHERE id_empresa = ?
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            int indice = preencherDadosCadastrais(ps, empresa);
            ps.setInt(indice, empresa.getId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean atualizarStatus(Connection conexao, int id, StatusEmpresa status) throws SQLException {
        String sql = "UPDATE TB_EMPRESA SET status_empresa = ? WHERE id_empresa = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        }
    }

    // Mesma ordem de colunas no INSERT e no UPDATE; devolve o próximo índice livre
    private int preencherDadosCadastrais(PreparedStatement ps, Empresa empresa) throws SQLException {
        IdentidadeVisual visual = empresa.getIdentidadeVisual();
        ps.setString(1, empresa.getRazaoSocial());
        ps.setString(2, empresa.getNomeFantasia());
        ps.setString(3, empresa.getCnpj());
        ps.setString(4, empresa.getEmail());
        JdbcUtils.definirTexto(ps, 5, empresa.getTelefone());
        ps.setString(6, visual.corPrincipal());
        ps.setString(7, visual.tipografia().name());
        ps.setString(8, visual.formaBotoes().name());
        return 9;
    }

    private Optional<Empresa> buscarUma(Connection conexao, String sql, Object parametro) throws SQLException {
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setObject(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(montar(rs)) : Optional.empty();
            }
        }
    }

    private Empresa montar(ResultSet rs) throws SQLException {
        IdentidadeVisual visual = new IdentidadeVisual(
                JdbcUtils.lerTexto(rs, "cor_principal_empresa"),
                Tipografia.valueOf(rs.getString("tipografia_empresa")),
                FormaBotoes.valueOf(rs.getString("forma_botoes_empresa")));

        return new Empresa(
                rs.getInt("id_empresa"),
                rs.getString("razao_social_empresa"),
                rs.getString("nome_empresa"),
                JdbcUtils.lerTexto(rs, "cnpj_empresa"),
                rs.getString("email_empresa"),
                rs.getString("telefone_empresa"),
                visual,
                StatusEmpresa.valueOf(rs.getString("status_empresa")),
                JdbcUtils.lerData(rs, "data_cadastro_empresa"));
    }
}
