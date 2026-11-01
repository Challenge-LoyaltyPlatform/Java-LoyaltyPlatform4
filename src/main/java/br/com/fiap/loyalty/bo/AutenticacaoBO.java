package br.com.fiap.loyalty.bo;

import br.com.fiap.loyalty.dao.AcessoPainelDAO;
import br.com.fiap.loyalty.dao.EmpresaDAO;
import br.com.fiap.loyalty.dto.EmpresaResumoResponse;
import br.com.fiap.loyalty.dto.LoginRequest;
import br.com.fiap.loyalty.dto.LoginResponse;
import br.com.fiap.loyalty.dto.UsuarioLogadoResponse;
import br.com.fiap.loyalty.exception.AcessoNegadoException;
import br.com.fiap.loyalty.exception.NaoAutorizadoException;
import br.com.fiap.loyalty.factory.ConnectionFactory;
import br.com.fiap.loyalty.model.AcessoPainel;
import br.com.fiap.loyalty.model.Empresa;
import br.com.fiap.loyalty.security.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoBO {

    private static final String CREDENCIAIS_INCORRETAS = "E-mail ou senha incorretos.";

    private final ConnectionFactory connectionFactory;
    private final AcessoPainelDAO acessoPainelDAO;
    private final EmpresaDAO empresaDAO;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    // Comparado quando o e-mail não existe: o tempo de resposta não revela quais e-mails têm conta
    private final String hashFicticio;

    public AutenticacaoBO(ConnectionFactory connectionFactory, AcessoPainelDAO acessoPainelDAO, EmpresaDAO empresaDAO,
                          PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.connectionFactory = connectionFactory;
        this.acessoPainelDAO = acessoPainelDAO;
        this.empresaDAO = empresaDAO;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.hashFicticio = passwordEncoder.encode("senha-que-nao-pertence-a-ninguem");
    }

    // Conta encontrada + empresa de contexto (só para o perfil EMPRESA)
    private record ContaPainel(AcessoPainel acesso, Empresa empresa) {
    }

    public LoginResponse login(LoginRequest dados) {
        Optional<ContaPainel> conta = connectionFactory.executar(conexao -> {
            Optional<AcessoPainel> acesso = acessoPainelDAO.buscarPorEmail(conexao, dados.email());
            if (acesso.isEmpty()) {
                return Optional.empty();
            }
            Empresa empresa = acesso.get().isAdmin()
                    ? null
                    : empresaDAO.buscarPorId(conexao, acesso.get().getIdEmpresa()).orElse(null);
            return Optional.of(new ContaPainel(acesso.get(), empresa));
        });

        // O BCrypt roda fora do bloco acima para não segurar a conexão do pool
        if (conta.isEmpty()) {
            passwordEncoder.matches(dados.senha(), hashFicticio);
            throw new NaoAutorizadoException(CREDENCIAIS_INCORRETAS);
        }
        AcessoPainel acesso = conta.get().acesso();
        if (!passwordEncoder.matches(dados.senha(), acesso.getSenhaHash())) {
            throw new NaoAutorizadoException(CREDENCIAIS_INCORRETAS);
        }

        EmpresaResumoResponse empresaDeContexto = null;
        if (!acesso.isAdmin()) {
            Empresa empresa = conta.get().empresa();
            // Inativar a empresa tira o acesso dela à plataforma
            if (empresa == null || !empresa.estaAtiva()) {
                throw new AcessoNegadoException(
                        "O acesso da sua empresa à plataforma está inativo. Fale com a equipe da Loyalty.");
            }
            empresaDeContexto = new EmpresaResumoResponse(empresa.getId(), empresa.getNomeFantasia());
        }

        UsuarioLogadoResponse usuario = new UsuarioLogadoResponse(
                acesso.getId(), acesso.getNome(), acesso.getEmail(), acesso.getPerfil(), empresaDeContexto);
        return new LoginResponse(tokenService.gerar(acesso), usuario);
    }
}
