package br.com.fiap.loyalty.dto;

import br.com.fiap.loyalty.model.PerfilAcesso;
import com.fasterxml.jackson.annotation.JsonInclude;

// O perfil ADMIN não tem empresa de contexto: o campo "empresa" nem aparece no JSON
public record UsuarioLogadoResponse(
        Integer id,
        String nome,
        String email,
        PerfilAcesso perfil,
        @JsonInclude(JsonInclude.Include.NON_NULL) EmpresaResumoResponse empresa) {
}
