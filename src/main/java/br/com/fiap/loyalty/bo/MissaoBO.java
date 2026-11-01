package br.com.fiap.loyalty.bo;

import br.com.fiap.loyalty.dao.CampanhaDAO;
import br.com.fiap.loyalty.dao.MissaoDAO;
import br.com.fiap.loyalty.dto.MissaoRequest;
import br.com.fiap.loyalty.dto.MissaoResponse;
import br.com.fiap.loyalty.exception.DadosInvalidosException;
import br.com.fiap.loyalty.exception.NaoEncontradoException;
import br.com.fiap.loyalty.exception.RegraNegocioException;
import br.com.fiap.loyalty.factory.ConnectionFactory;
import br.com.fiap.loyalty.model.Campanha;
import br.com.fiap.loyalty.model.Missao;
import br.com.fiap.loyalty.model.StatusCampanha;
import br.com.fiap.loyalty.model.StatusMissao;
import br.com.fiap.loyalty.security.UsuarioAutenticado;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class MissaoBO {

    private final ConnectionFactory connectionFactory;
    private final MissaoDAO missaoDAO;
    private final CampanhaDAO campanhaDAO;

    public MissaoBO(ConnectionFactory connectionFactory, MissaoDAO missaoDAO, CampanhaDAO campanhaDAO) {
        this.connectionFactory = connectionFactory;
        this.missaoDAO = missaoDAO;
        this.campanhaDAO = campanhaDAO;
    }

    private record MissaoDaCampanha(Missao missao, Campanha campanha) {
    }

    public List<MissaoResponse> listarPorCampanha(UsuarioAutenticado usuario, int idCampanha) {
        return connectionFactory.executar(conexao -> {
            buscarCampanhaAutorizada(conexao, usuario, idCampanha);
            return missaoDAO.listarPorCampanha(conexao, idCampanha).stream().map(MissaoResponse::de).toList();
        });
    }

    public MissaoResponse buscar(UsuarioAutenticado usuario, int id) {
        return connectionFactory.executar(conexao -> MissaoResponse.de(buscarAutorizada(conexao, usuario, id).missao()));
    }

    // Missão nasce ATIVA; a campanha continua no status em que estava
    public MissaoResponse cadastrar(UsuarioAutenticado usuario, int idCampanha, MissaoRequest dados) {
        return connectionFactory.executarEmTransacao(conexao -> {
            Campanha campanha = buscarCampanhaAutorizada(conexao, usuario, idCampanha);
            if (campanha.estaEncerrada()) {
                throw new RegraNegocioException("Campanha encerrada não recebe novas missões.");
            }

            Missao missao = new Missao(null, idCampanha, dados.nome(), dados.descricao(),
                    dados.meta(), dados.pontosRecompensa(), StatusMissao.ATIVA);
            missao.setId(missaoDAO.inserir(conexao, missao));

            return MissaoResponse.de(missao);
        });
    }

    public MissaoResponse atualizar(UsuarioAutenticado usuario, int id, MissaoRequest dados) {
        return connectionFactory.executarEmTransacao(conexao -> {
            MissaoDaCampanha encontrada = buscarAlteravel(conexao, usuario, id);
            Missao missao = encontrada.missao();

            missao.setNome(dados.nome());
            missao.setDescricao(dados.descricao());
            missao.setMeta(dados.meta());
            missao.setPontosRecompensa(dados.pontosRecompensa());
            missaoDAO.atualizar(conexao, missao);

            return MissaoResponse.de(missao);
        });
    }

    public MissaoResponse alterarStatus(UsuarioAutenticado usuario, int id, StatusMissao novoStatus) {
        if (novoStatus == StatusMissao.EXCLUIDA) {
            throw new DadosInvalidosException("Para excluir a missão, use DELETE /missoes/{id}.",
                    Map.of("status", "Use ATIVA ou INATIVA."));
        }
        return connectionFactory.executarEmTransacao(conexao -> {
            MissaoDaCampanha encontrada = buscarAlteravel(conexao, usuario, id);
            Missao missao = encontrada.missao();

            if (novoStatus == StatusMissao.ATIVA) {
                missao.ativar();
            } else {
                garantirOutraMissaoAtiva(conexao, encontrada);
                missao.desativar();
            }
            missaoDAO.atualizarStatus(conexao, id, missao.getStatus());

            return MissaoResponse.de(missao);
        });
    }

    // RN25 — exclusão lógica: a missão some para a empresa, mas o histórico de conclusões fica no banco
    public void excluir(UsuarioAutenticado usuario, int id) {
        connectionFactory.executarEmTransacao(conexao -> {
            MissaoDaCampanha encontrada = buscarAlteravel(conexao, usuario, id);
            garantirOutraMissaoAtiva(conexao, encontrada);

            encontrada.missao().excluir();
            missaoDAO.atualizarStatus(conexao, id, StatusMissao.EXCLUIDA);
            return null;
        });
    }

    // Campanha ativa precisa de ao menos uma missão ativa (mesma regra da ativação)
    private void garantirOutraMissaoAtiva(Connection conexao, MissaoDaCampanha encontrada) throws SQLException {
        Campanha campanha = encontrada.campanha();
        if (campanha.getStatus() == StatusCampanha.ATIVA
                && encontrada.missao().estaAtiva()
                && missaoDAO.contarPorCampanha(conexao, campanha.getId()).ativas() <= 1) {
            throw new RegraNegocioException(
                    "Esta é a única missão ativa da campanha. Inative a campanha antes ou ative outra missão.");
        }
    }

    // Missão de campanha encerrada fica congelada, como a própria campanha (RN23)
    private MissaoDaCampanha buscarAlteravel(Connection conexao, UsuarioAutenticado usuario, int id) throws SQLException {
        MissaoDaCampanha encontrada = buscarAutorizada(conexao, usuario, id);
        if (encontrada.campanha().estaEncerrada()) {
            throw new RegraNegocioException("Missões de campanha encerrada não podem ser alteradas.");
        }
        return encontrada;
    }

    // Missão excluída é tratada como inexistente para quem usa a API
    private MissaoDaCampanha buscarAutorizada(Connection conexao, UsuarioAutenticado usuario, int id) throws SQLException {
        Missao missao = missaoDAO.buscarPorId(conexao, id)
                .filter(encontrada -> !encontrada.foiExcluida())
                .orElseThrow(() -> new NaoEncontradoException("Missão não encontrada."));
        Campanha campanha = buscarCampanhaAutorizada(conexao, usuario, missao.getIdCampanha());
        return new MissaoDaCampanha(missao, campanha);
    }

    private Campanha buscarCampanhaAutorizada(Connection conexao, UsuarioAutenticado usuario, int idCampanha)
            throws SQLException {
        Campanha campanha = campanhaDAO.buscarPorId(conexao, idCampanha)
                .orElseThrow(() -> new NaoEncontradoException("Campanha não encontrada."));
        usuario.exigirAcessoAEmpresa(campanha.getIdEmpresa());
        return campanha;
    }
}
