package br.com.fiap.loyalty.factory;

import java.sql.Connection;
import java.sql.SQLException;

// Trecho de código que usa uma conexão aberta pela ConnectionFactory
@FunctionalInterface
public interface OperacaoBanco<T> {

    T executar(Connection conexao) throws SQLException;
}
