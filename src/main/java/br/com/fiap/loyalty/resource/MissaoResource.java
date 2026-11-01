package br.com.fiap.loyalty.resource;

import br.com.fiap.loyalty.bo.MissaoBO;
import br.com.fiap.loyalty.dto.AlteracaoStatusMissaoRequest;
import br.com.fiap.loyalty.dto.MissaoRequest;
import br.com.fiap.loyalty.dto.MissaoResponse;
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

// Missões das campanhas
@RestController
public class MissaoResource {

    private final MissaoBO missaoBO;

    public MissaoResource(MissaoBO missaoBO) {
        this.missaoBO = missaoBO;
    }

    @GetMapping("/campanhas/{idCampanha}/missoes")
    public List<MissaoResponse> listar(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                       @PathVariable int idCampanha) {
        return missaoBO.listarPorCampanha(usuario, idCampanha);
    }

    @PostMapping("/campanhas/{idCampanha}/missoes")
    public ResponseEntity<MissaoResponse> cadastrar(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                                    @PathVariable int idCampanha,
                                                    @Valid @RequestBody MissaoRequest dados) {
        MissaoResponse criada = missaoBO.cadastrar(usuario, idCampanha, dados);
        URI local = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/missoes/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(local).body(criada);
    }

    @GetMapping("/missoes/{id}")
    public MissaoResponse buscar(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                 @PathVariable int id) {
        return missaoBO.buscar(usuario, id);
    }

    @PutMapping("/missoes/{id}")
    public MissaoResponse atualizar(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                    @PathVariable int id,
                                    @Valid @RequestBody MissaoRequest dados) {
        return missaoBO.atualizar(usuario, id, dados);
    }

    @PatchMapping("/missoes/{id}/status")
    public MissaoResponse alterarStatus(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                        @PathVariable int id,
                                        @Valid @RequestBody AlteracaoStatusMissaoRequest dados) {
        return missaoBO.alterarStatus(usuario, id, dados.status());
    }

    // Exclusão lógica (status EXCLUIDA): 204 sem corpo
    @DeleteMapping("/missoes/{id}")
    public ResponseEntity<Void> excluir(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                        @PathVariable int id) {
        missaoBO.excluir(usuario, id);
        return ResponseEntity.noContent().build();
    }
}
