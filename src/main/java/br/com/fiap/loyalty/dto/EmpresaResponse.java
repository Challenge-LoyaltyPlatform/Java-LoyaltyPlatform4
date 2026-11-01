package br.com.fiap.loyalty.dto;

import br.com.fiap.loyalty.model.Empresa;
import br.com.fiap.loyalty.model.IdentidadeVisual;
import br.com.fiap.loyalty.model.StatusEmpresa;

import java.time.LocalDate;

// Empresa como o front espera: dados cadastrais + o que o servidor gera ou calcula
public record EmpresaResponse(
        Integer id,
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String email,
        String telefone,
        IdentidadeVisual identidadeVisual,
        StatusEmpresa status,
        LocalDate dataCadastro,
        int campanhasAtivas,
        int usuariosVinculados) {

    public static EmpresaResponse de(Empresa empresa, int campanhasAtivas, int usuariosVinculados) {
        return new EmpresaResponse(
                empresa.getId(),
                empresa.getRazaoSocial(),
                empresa.getNomeFantasia(),
                empresa.getCnpj(),
                empresa.getEmail(),
                empresa.getTelefone(),
                empresa.getIdentidadeVisual(),
                empresa.getStatus(),
                empresa.getDataCadastro(),
                campanhasAtivas,
                usuariosVinculados);
    }
}
