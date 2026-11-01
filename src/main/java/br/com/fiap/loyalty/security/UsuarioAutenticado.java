package br.com.fiap.loyalty.security;

import br.com.fiap.loyalty.exception.AcessoNegadoException;
import br.com.fiap.loyalty.model.PerfilAcesso;

import java.util.Objects;

// Quem fez a requisição, lido do token pelo AutenticacaoFilter
public record UsuarioAutenticado(int id, String nome, String email, PerfilAcesso perfil, Integer idEmpresa) {

    // Nome do atributo da requisição onde o filtro guarda este objeto
    public static final String ATRIBUTO = "usuarioAutenticado";

    public boolean isAdmin() {
        return perfil == PerfilAcesso.ADMIN;
    }

    // RN21 — somente o Administrador da Loyalty gerencia as empresas clientes
    public void exigirAdmin() {
        if (!isAdmin()) {
            throw new AcessoNegadoException("Somente o administrador da Loyalty pode fazer isso.");
        }
    }

    // O administrador vê todas as empresas; a empresa cliente só vê a própria
    public void exigirAcessoAEmpresa(int idEmpresaAlvo) {
        if (!isAdmin() && !Objects.equals(idEmpresa, idEmpresaAlvo)) {
            throw new AcessoNegadoException("Você só pode acessar os dados da sua empresa.");
        }
    }
}
