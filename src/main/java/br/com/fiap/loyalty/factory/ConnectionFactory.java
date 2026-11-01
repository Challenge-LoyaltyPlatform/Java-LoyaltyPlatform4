package br.com.fiap.loyalty.factory;

import br.com.fiap.loyalty.exception.PersistenciaException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Padrão Connection Factory: único ponto da aplicação que abre conexões com o Oracle.
 * <p>
 * Em vez de chamar o DriverManager a cada requisição, usa o pool HikariCP que o Spring Boot
 * monta com os dados do application.properties (URL, usuário e senha vêm de variáveis de ambiente).
 * As camadas BO usam {@link #executar} para leituras e {@link #executarEmTransacao} para gravações.
 */
@Component
public class ConnectionFactory {

    private final DataSource dataSource;

    public ConnectionFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Leitura: abre a conexão, executa e devolve a conexão ao pool
    public <T> T executar(OperacaoBanco<T> operacao) {
        try (Connection conexao = getConnection()) {
            return operacao.executar(conexao);
        } catch (SQLException e) {
            throw new PersistenciaException(e);
        }
    }

    // Gravação: tudo o que a operação fizer é confirmado junto ou desfeito junto
    public <T> T executarEmTransacao(OperacaoBanco<T> operacao) {
        try (Connection conexao = getConnection()) {
            conexao.setAutoCommit(false);
            try {
                T resultado = operacao.executar(conexao);
                conexao.commit();
                return resultado;
            } catch (SQLException | RuntimeException e) {
                conexao.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException(e);
        }
    }
}
