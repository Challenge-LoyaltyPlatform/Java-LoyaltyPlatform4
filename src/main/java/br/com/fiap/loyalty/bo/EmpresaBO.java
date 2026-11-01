package br.com.fiap.loyalty.bo;

import br.com.fiap.loyalty.dao.CampanhaDAO;
import br.com.fiap.loyalty.dao.EmpresaDAO;
import br.com.fiap.loyalty.dao.EmpresaUsuarioDAO;
import br.com.fiap.loyalty.dto.EmpresaRequest;
import br.com.fiap.loyalty.dto.EmpresaResponse;
import br.com.fiap.loyalty.exception.NaoEncontradoException;
import br.com.fiap.loyalty.exception.RegraNegocioException;
import br.com.fiap.loyalty.factory.ConnectionFactory;
import br.com.fiap.loyalty.model.Empresa;
import br.com.fiap.loyalty.model.StatusEmpresa;
import br.com.fiap.loyalty.security.UsuarioAutenticado;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmpresaBO {

    private final ConnectionFactory connectionFactory;
    private final EmpresaDAO empresaDAO;
    private final CampanhaDAO campanhaDAO;
    private final EmpresaUsuarioDAO empresaUsuarioDAO;
    private final Clock relogio;

    public EmpresaBO(ConnectionFactory connectionFactory, EmpresaDAO empresaDAO, CampanhaDAO campanhaDAO,
                     EmpresaUsuarioDAO empresaUsuarioDAO, Clock relogio) {
        this.connectionFactory = connectionFactory;
        this.empresaDAO = empresaDAO;
        this.campanhaDAO = campanhaDAO;
        this.empresaUsuarioDAO = empresaUsuarioDAO;
        this.relogio = relogio;
    }

    // O front filtra e busca localmente, então devolve todas (lista vazia, nunca 404)
    public List<EmpresaResponse> listar(UsuarioAutenticado usuario) {
        usuario.exigirAdmin();
        return connectionFactory.executar(conexao -> {
            List<Empresa> empresas = empresaDAO.listar(conexao);
            // Duas consultas agrupadas em vez de duas por empresa
            Map<Integer, Integer> campanhasAtivas = campanhaDAO.contarAtivasPorEmpresa(conexao);
            Map<Integer, Integer> usuariosVinculados = empresaUsuarioDAO.contarUsuariosPorEmpresa(conexao);

            return empresas.stream()
                    .map(empresa -> EmpresaResponse.de(empresa,
                            campanhasAtivas.getOrDefault(empresa.getId(), 0),
                            usuariosVinculados.getOrDefault(empresa.getId(), 0)))
                    .toList();
        });
    }

    public EmpresaResponse buscar(UsuarioAutenticado usuario, int id) {
        usuario.exigirAcessoAEmpresa(id);
        return connectionFactory.executar(conexao -> montarResposta(conexao, buscarOuFalhar(conexao, id)));
    }

    // Empresa nasce ATIVA, com a data de hoje
    public EmpresaResponse cadastrar(UsuarioAutenticado usuario, EmpresaRequest dados) {
        usuario.exigirAdmin();
        return connectionFactory.executarEmTransacao(conexao -> {
            verificarUnicidade(conexao, dados, null);

            Empresa empresa = new Empresa(null, dados.razaoSocial(), dados.nomeFantasia(), dados.cnpj(),
                    dados.email(), dados.telefone(), dados.identidadeVisual().paraModelo(),
                    StatusEmpresa.ATIVA, LocalDate.now(relogio));
            empresa.setId(empresaDAO.inserir(conexao, empresa));

            return EmpresaResponse.de(empresa, 0, 0);
        });
    }

    // Substitui dados cadastrais e identidade visual; status muda só pelo PATCH
    public EmpresaResponse atualizar(UsuarioAutenticado usuario, int id, EmpresaRequest dados) {
        usuario.exigirAdmin();
        return connectionFactory.executarEmTransacao(conexao -> {
            Empresa empresa = buscarOuFalhar(conexao, id);
            verificarUnicidade(conexao, dados, id);

            empresa.setRazaoSocial(dados.razaoSocial());
            empresa.setNomeFantasia(dados.nomeFantasia());
            empresa.setCnpj(dados.cnpj());
            empresa.setEmail(dados.email());
            empresa.setTelefone(dados.telefone());
            empresa.setIdentidadeVisual(dados.identidadeVisual().paraModelo());
            empresaDAO.atualizar(conexao, empresa);

            return montarResposta(conexao, empresa);
        });
    }

    // Inativar encerra as campanhas ativas na mesma transação (a pontuação dos usuários fica, RN16).
    // Reativar devolve o acesso, mas não reativa campanhas encerradas (RN23)
    public EmpresaResponse alterarStatus(UsuarioAutenticado usuario, int id, StatusEmpresa novoStatus) {
        usuario.exigirAdmin();
        return connectionFactory.executarEmTransacao(conexao -> {
            Empresa empresa = buscarOuFalhar(conexao, id);

            if (novoStatus == StatusEmpresa.ATIVA) {
                empresa.ativar();
            } else {
                empresa.desativar();
                campanhaDAO.encerrarAtivasDaEmpresa(conexao, id);
            }
            empresaDAO.atualizarStatus(conexao, id, empresa.getStatus());

            return montarResposta(conexao, empresa);
        });
    }

    // CNPJ e e-mail são únicos (constraints UNIQUE da TB_EMPRESA). Checar antes permite
    // dizer ao usuário qual empresa já usa o dado; a constraint continua valendo como garantia
    private void verificarUnicidade(Connection conexao, EmpresaRequest dados, Integer idIgnorado) throws SQLException {
        Map<String, String> campos = new LinkedHashMap<>();

        empresaDAO.buscarPorCnpj(conexao, dados.cnpj())
                .filter(outra -> !outra.getId().equals(idIgnorado))
                .ifPresent(outra -> campos.put("cnpj", "Este CNPJ já está cadastrado (" + outra.getNomeFantasia()
                        + "). Confira o número ou reative a empresa existente."));

        empresaDAO.buscarPorEmail(conexao, dados.email())
                .filter(outra -> !outra.getId().equals(idIgnorado))
                .ifPresent(outra -> campos.put("email", "Este e-mail já é usado por " + outra.getNomeFantasia() + "."));

        if (!campos.isEmpty()) {
            throw new RegraNegocioException("Já existe empresa com esses dados.", campos);
        }
    }

    private Empresa buscarOuFalhar(Connection conexao, int id) throws SQLException {
        return empresaDAO.buscarPorId(conexao, id)
                .orElseThrow(() -> new NaoEncontradoException("Empresa não encontrada."));
    }

    // Resposta completa, com os totais recalculados (o front usa o retorno para atualizar a tela)
    private EmpresaResponse montarResposta(Connection conexao, Empresa empresa) throws SQLException {
        return EmpresaResponse.de(empresa,
                campanhaDAO.contarAtivas(conexao, empresa.getId()),
                empresaUsuarioDAO.contarUsuarios(conexao, empresa.getId()));
    }
}
