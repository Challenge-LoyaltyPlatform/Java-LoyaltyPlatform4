package br.com.fiap.loyalty.exception;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

// Falha ao falar com o banco. A SQLException original fica como causa para o log
public class PersistenciaException extends RuntimeException {

    public PersistenciaException(SQLException causa) {
        super("Falha ao acessar o banco de dados.", causa);
    }

    // ORA-00001 (UNIQUE), ORA-02290 (CHECK), ORA-02291/02292 (FK) chegam como esta subclasse
    public boolean violouRestricao() {
        return getCause() instanceof SQLIntegrityConstraintViolationException;
    }
}
