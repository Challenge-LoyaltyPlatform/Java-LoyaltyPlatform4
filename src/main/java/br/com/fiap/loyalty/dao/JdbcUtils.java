package br.com.fiap.loyalty.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

// Conversões repetidas entre os tipos do Java e as colunas do Oracle (datas e campos opcionais)
final class JdbcUtils {

    private JdbcUtils() {
        // classe utilitária: não instanciável
    }

    static LocalDate lerData(ResultSet rs, String coluna) throws SQLException {
        Date data = rs.getDate(coluna);
        return data == null ? null : data.toLocalDate();
    }

    // Colunas CHAR do Oracle completam com espaços à direita
    static String lerTexto(ResultSet rs, String coluna) throws SQLException {
        String valor = rs.getString(coluna);
        return valor == null ? null : valor.trim();
    }

    static void definirData(PreparedStatement ps, int indice, LocalDate data) throws SQLException {
        if (data == null) {
            ps.setNull(indice, Types.DATE);
        } else {
            ps.setDate(indice, Date.valueOf(data));
        }
    }

    static void definirTexto(PreparedStatement ps, int indice, String texto) throws SQLException {
        if (texto == null) {
            ps.setNull(indice, Types.VARCHAR);
        } else {
            ps.setString(indice, texto);
        }
    }

    // Lê o id gerado pela coluna IDENTITY depois de um INSERT
    static int lerIdGerado(PreparedStatement ps) throws SQLException {
        try (ResultSet chaves = ps.getGeneratedKeys()) {
            if (!chaves.next()) {
                throw new SQLException("O banco não devolveu o id gerado.");
            }
            return chaves.getInt(1);
        }
    }
}
