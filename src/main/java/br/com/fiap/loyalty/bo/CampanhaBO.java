package br.com.fiap.loyalty.bo;

import br.com.fiap.loyalty.dao.CampanhaDAO;
import br.com.fiap.loyalty.dao.EmpresaDAO;
import br.com.fiap.loyalty.dao.MissaoDAO;
import br.com.fiap.loyalty.dao.UsuarioMissaoDAO;
import br.com.fiap.loyalty.dto.CampanhaRequest;
import br.com.fiap.loyalty.dto.CampanhaResponse;
import br.com.fiap.loyalty.dto.MissaoResponse;
import br.com.fiap.loyalty.exception.DadosInvalidosException;
import br.com.fiap.loyalty.exception.NaoEncontradoException;
import br.com.fiap.loyalty.exception.RegraNegocioException;
import br.com.fiap.loyalty.factory.ConnectionFactory;
import br.com.fiap.loyalty.model.Campanha;
import br.com.fiap.loyalty.model.Empresa;
import br.com.fiap.loyalty.model.StatusCampanha;
import br.com.fiap.loyalty.security.UsuarioAutenticado;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class CampanhaBO {

    private final ConnectionFactory connectionFactory;
    private final CampanhaDAO campanhaDAO;
    private final MissaoDAO missaoDAO;
    private final EmpresaDAO empresaDAO;
    private final UsuarioMissaoDAO usuarioMissaoDAO;
    private final Clock relogio;

    public CampanhaBO(ConnectionFactory connectionFactory, CampanhaDAO campanhaDAO, MissaoDAO missaoDAO,
                      EmpresaDAO empresaDAO, UsuarioMissaoDAO usuarioMissaoDAO, Clock relogio) {
        this.connectionFactory = connectionFactory;
        this.campanhaDAO = campanhaDAO;
        this.missaoDAO = missaoDAO;
        this.empresaDAO = empresaDAO;
        this.usuarioMissaoDAO = usuarioMissaoDAO;
        this.relogio = relogio;
    }

    public List<CampanhaResponse> listarPorEmpresa(UsuarioAutenticado usuario, int idEmpresa) {
        usuario.exigirAcessoAEmpresa(idEmpresa);
        return connectionFactory.executar(conexao -> {
            buscarEmpresaOuFalhar(conexao, idEmpresa);
            Map<Integer, MissaoDAO.Contagem> contagens = missaoDAO.contarPorCampanhasDaEmpresa(conexao, idEmpresa);

            return campanhaDAO.listarPorEmpresa(conexao, idEmpresa).stream()
                    .map(campanha -> CampanhaResponse.resumo(campanha,
                            contagens.getOrDefault(campanha.getId(), MissaoDAO.Contagem.VAZIA)))
                    .toList();
        });
    }

    public CampanhaResponse buscar(UsuarioAutenticado usuario, int id) {
        return connectionFactory.executar(conexao -> montarDetalhe(conexao, buscarAutorizada(conexao, usuario, id)));
    }

    // Campanha nasce INATIVA (rascunho): só é ativada depois de ter missões
    public CampanhaResponse cadastrar(UsuarioAutenticado usuario, int idEmpresa, CampanhaRequest dados) {
        usuario.exigirAcessoAEmpresa(idEmpresa);
        validarPeriodo(dados);
        return connectionFactory.executarEmTransacao(conexao -> {
            Empresa empresa = buscarEmpresaOuFalhar(conexao, idEmpresa);
            if (!empresa.estaAtiva()) {
                throw new RegraNegocioException("Empresa inativa não pode criar campanhas.");
            }

            Campanha campanha = new Campanha(null, idEmpresa, dados.nome(), dados.descricao(),
                    dados.dataInicio(), dados.dataFim(), StatusCampanha.INATIVA);
            campanha.setId(campanhaDAO.inserir(conexao, campanha));

            return CampanhaResponse.detalhe(campanha, List.of());
        });
    }

    public CampanhaResponse atualizar(UsuarioAutenticado usuario, int id, CampanhaRequest dados) {
        validarPeriodo(dados);
        return connectionFactory.executarEmTransacao(conexao -> {
            Campanha campanha = buscarAutorizada(conexao, usuario, id);
            // RN23 — campanha encerrada é terminal
            if (campanha.estaEncerrada()) {
                throw new RegraNegocioException("Campanha encerrada não pode ser editada.");
            }

            campanha.setNome(dados.nome());
            campanha.setDescricao(dados.descricao());
            campanha.setDataInicio(dados.dataInicio());
            campanha.setDataFim(dados.dataFim());
            campanhaDAO.atualizar(conexao, campanha);

            return montarDetalhe(conexao, campanha);
        });
    }

    public CampanhaResponse alterarStatus(UsuarioAutenticado usuario, int id, StatusCampanha novoStatus) {
        return connectionFactory.executarEmTransacao(conexao -> {
            Campanha campanha = buscarAutorizada(conexao, usuario, id);

            switch (novoStatus) {
                case ATIVA -> {
                    campanha.ativar();
                    validarAtivacao(conexao, campanha);
                }
                case INATIVA -> campanha.desativar();
                case ENCERRADA -> campanha.encerrar();
            }
            campanhaDAO.atualizarStatus(conexao, id, campanha.getStatus());

            return montarDetalhe(conexao, campanha);
        });
    }

    // Exclusão física só para rascunho: campanha inativa que nunca teve participantes.
    // Com histórico de pontos, o caminho é encerrar (RN16)
    public void excluir(UsuarioAutenticado usuario, int id) {
        connectionFactory.executarEmTransacao(conexao -> {
            Campanha campanha = buscarAutorizada(conexao, usuario, id);
            if (campanha.getStatus() != StatusCampanha.INATIVA) {
                throw new RegraNegocioException(
                        "Só campanhas inativas podem ser excluídas. Para tirar do ar uma campanha ativa, encerre-a.");
            }
            if (usuarioMissaoDAO.contarParticipacoesNaCampanha(conexao, id) > 0) {
                throw new RegraNegocioException(
                        "Esta campanha já teve participantes. Encerre-a em vez de excluir, para preservar o histórico de pontos.");
            }

            missaoDAO.excluirPorCampanha(conexao, id);
            campanhaDAO.excluir(conexao, id);
            return null;
        });
    }

    // RN09 — para ativar: empresa ativa, ao menos uma missão ativa e período ainda vigente
    private void validarAtivacao(Connection conexao, Campanha campanha) throws SQLException {
        Empresa empresa = buscarEmpresaOuFalhar(conexao, campanha.getIdEmpresa());
        if (!empresa.estaAtiva()) {
            throw new RegraNegocioException("A empresa está inativa. Reative a empresa antes de ativar campanhas.");
        }
        if (missaoDAO.contarPorCampanha(conexao, campanha.getId()).ativas() == 0) {
            throw new RegraNegocioException("Cadastre ao menos uma missão ativa antes de ativar a campanha.");
        }
        if (campanha.periodoTerminouAntesDe(LocalDate.now(relogio))) {
            throw new RegraNegocioException("A data de fim da campanha já passou. Ajuste o período antes de ativar.");
        }
    }

    private void validarPeriodo(CampanhaRequest dados) {
        if (dados.dataFim() != null && dados.dataFim().isBefore(dados.dataInicio())) {
            throw new DadosInvalidosException("Os dados enviados são inválidos.",
                    Map.of("dataFim", "A data de fim deve ser igual ou posterior à data de início."));
        }
    }

    private Campanha buscarAutorizada(Connection conexao, UsuarioAutenticado usuario, int id) throws SQLException {
        Campanha campanha = campanhaDAO.buscarPorId(conexao, id)
                .orElseThrow(() -> new NaoEncontradoException("Campanha não encontrada."));
        usuario.exigirAcessoAEmpresa(campanha.getIdEmpresa());
        return campanha;
    }

    private Empresa buscarEmpresaOuFalhar(Connection conexao, int idEmpresa) throws SQLException {
        return empresaDAO.buscarPorId(conexao, idEmpresa)
                .orElseThrow(() -> new NaoEncontradoException("Empresa não encontrada."));
    }

    private CampanhaResponse montarDetalhe(Connection conexao, Campanha campanha) throws SQLException {
        List<MissaoResponse> missoes = missaoDAO.listarPorCampanha(conexao, campanha.getId()).stream()
                .map(MissaoResponse::de)
                .toList();
        return CampanhaResponse.detalhe(campanha, missoes);
    }
}
