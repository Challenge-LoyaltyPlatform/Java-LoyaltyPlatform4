package br.com.fiap.loyalty.resource;

import br.com.fiap.loyalty.bo.EmpresaBO;
import br.com.fiap.loyalty.dto.AlteracaoStatusEmpresaRequest;
import br.com.fiap.loyalty.dto.EmpresaRequest;
import br.com.fiap.loyalty.dto.EmpresaResponse;
import br.com.fiap.loyalty.security.UsuarioAutenticado;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// Empresas clientes (painel do administrador). Não há DELETE: empresa é inativada, não excluída
@RestController
@RequestMapping("/empresas")
public class EmpresaResource {

    private final EmpresaBO empresaBO;

    public EmpresaResource(EmpresaBO empresaBO) {
        this.empresaBO = empresaBO;
    }

    @GetMapping
    public List<EmpresaResponse> listar(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario) {
        return empresaBO.listar(usuario);
    }

    @GetMapping("/{id}")
    public EmpresaResponse buscar(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                  @PathVariable int id) {
        return empresaBO.buscar(usuario, id);
    }

    @PostMapping
    public ResponseEntity<EmpresaResponse> cadastrar(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                                     @Valid @RequestBody EmpresaRequest dados) {
        EmpresaResponse criada = empresaBO.cadastrar(usuario, dados);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(local).body(criada);
    }

    @PutMapping("/{id}")
    public EmpresaResponse atualizar(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                     @PathVariable int id,
                                     @Valid @RequestBody EmpresaRequest dados) {
        return empresaBO.atualizar(usuario, id, dados);
    }

    @PatchMapping("/{id}/status")
    public EmpresaResponse alterarStatus(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                         @PathVariable int id,
                                         @Valid @RequestBody AlteracaoStatusEmpresaRequest dados) {
        return empresaBO.alterarStatus(usuario, id, dados.status());
    }
}
