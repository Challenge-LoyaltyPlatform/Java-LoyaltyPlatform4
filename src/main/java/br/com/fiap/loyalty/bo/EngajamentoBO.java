package br.com.fiap.loyalty.bo;

import br.com.fiap.loyalty.dao.EmpresaDAO;
import br.com.fiap.loyalty.dao.MetricasDAO;
import br.com.fiap.loyalty.dao.RankingDAO;
import br.com.fiap.loyalty.dto.ItemRankingResponse;
import br.com.fiap.loyalty.dto.MetricasEmpresaResponse;
import br.com.fiap.loyalty.exception.NaoEncontradoException;
import br.com.fiap.loyalty.factory.ConnectionFactory;
import br.com.fiap.loyalty.security.UsuarioAutenticado;
import br.com.fiap.loyalty.util.Numeros;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

// Ranking e métricas: as consultas de engajamento da entrega de banco expostas para o painel
@Service
public class EngajamentoBO {

    private final ConnectionFactory connectionFactory;
    private final EmpresaDAO empresaDAO;
    private final RankingDAO rankingDAO;
    private final MetricasDAO metricasDAO;

    public EngajamentoBO(ConnectionFactory connectionFactory, EmpresaDAO empresaDAO, RankingDAO rankingDAO,
                         MetricasDAO metricasDAO) {
        this.connectionFactory = connectionFactory;
        this.empresaDAO = empresaDAO;
        this.rankingDAO = rankingDAO;
        this.metricasDAO = metricasDAO;
    }

    public List<ItemRankingResponse> ranking(UsuarioAutenticado usuario, int idEmpresa) {
        usuario.exigirAcessoAEmpresa(idEmpresa);
        return connectionFactory.executar(conexao -> {
            exigirEmpresa(conexao, idEmpresa);
            return rankingDAO.listarPorEmpresa(conexao, idEmpresa);
        });
    }

    public MetricasEmpresaResponse metricas(UsuarioAutenticado usuario, int idEmpresa) {
        usuario.exigirAcessoAEmpresa(idEmpresa);
        return connectionFactory.executar(conexao -> {
            exigirEmpresa(conexao, idEmpresa);
            MetricasDAO.Engajamento engajamento = metricasDAO.buscarEngajamento(conexao, idEmpresa);

            return new MetricasEmpresaResponse(
                    idEmpresa,
                    engajamento.usuariosVinculados(),
                    engajamento.pontosTotais(),
                    engajamento.mediaPontos(),
                    engajamento.maiorPontuacao(),
                    engajamento.sequenciasAtivas(),
                    engajamento.sequenciasEmRisco(),
                    Numeros.percentual(engajamento.sequenciasAtivas(), engajamento.usuariosVinculados()),
                    metricasDAO.listarPorCampanha(conexao, idEmpresa));
        });
    }

    private void exigirEmpresa(Connection conexao, int idEmpresa) throws SQLException {
        if (empresaDAO.buscarPorId(conexao, idEmpresa).isEmpty()) {
            throw new NaoEncontradoException("Empresa não encontrada.");
        }
    }
}
