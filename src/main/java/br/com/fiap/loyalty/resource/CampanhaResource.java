package br.com.fiap.loyalty.resource;

import br.com.fiap.loyalty.bo.CampanhaBO;
import br.com.fiap.loyalty.dto.AlteracaoStatusCampanhaRequest;
import br.com.fiap.loyalty.dto.CampanhaRequest;
import br.com.fiap.loyalty.dto.CampanhaResponse;
import br.com.fiap.loyalty.security.UsuarioAutenticado;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// Campanhas de uma empresa (painel da empresa cliente; o administrador também acessa)
@RestController
public class CampanhaResource {

    private final CampanhaBO campanhaBO;

    public CampanhaResource(CampanhaBO campanhaBO) {
        this.campanhaBO = campanhaBO;
    }

    @GetMapping("/empresas/{idEmpresa}/campanhas")
    public List<CampanhaResponse> listar(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                         @PathVariable int idEmpresa) {
        return campanhaBO.listarPorEmpresa(usuario, idEmpresa);
    }

    @PostMapping("/empresas/{idEmpresa}/campanhas")
    public ResponseEntity<CampanhaResponse> cadastrar(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                                      @PathVariable int idEmpresa,
                                                      @Valid @RequestBody CampanhaRequest dados) {
        CampanhaResponse criada = campanhaBO.cadastrar(usuario, idEmpresa, dados);
        URI local = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/campanhas/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(local).body(criada);
    }

    @GetMapping("/campanhas/{id}")
    public CampanhaResponse buscar(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                   @PathVariable int id) {
        return campanhaBO.buscar(usuario, id);
    }

    @PutMapping("/campanhas/{id}")
    public CampanhaResponse atualizar(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                      @PathVariable int id,
                                      @Valid @RequestBody CampanhaRequest dados) {
        return campanhaBO.atualizar(usuario, id, dados);
    }

    @PatchMapping("/campanhas/{id}/status")
    public CampanhaResponse alterarStatus(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                          @PathVariable int id,
                                          @Valid @RequestBody AlteracaoStatusCampanhaRequest dados) {
        return campanhaBO.alterarStatus(usuario, id, dados.status());
    }

    @DeleteMapping("/campanhas/{id}")
    public ResponseEntity<Void> excluir(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                        @PathVariable int id) {
        campanhaBO.excluir(usuario, id);
        return ResponseEntity.noContent().build();
    }
}
