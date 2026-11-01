package br.com.fiap.loyalty.resource;

import br.com.fiap.loyalty.bo.EngajamentoBO;
import br.com.fiap.loyalty.dto.ItemRankingResponse;
import br.com.fiap.loyalty.dto.MetricasEmpresaResponse;
import br.com.fiap.loyalty.security.UsuarioAutenticado;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Ranking e métricas de engajamento de uma empresa (somente leitura)
@RestController
@RequestMapping("/empresas/{idEmpresa}")
public class EngajamentoResource {

    private final EngajamentoBO engajamentoBO;

    public EngajamentoResource(EngajamentoBO engajamentoBO) {
        this.engajamentoBO = engajamentoBO;
    }

    @GetMapping("/ranking")
    public List<ItemRankingResponse> ranking(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                             @PathVariable int idEmpresa) {
        return engajamentoBO.ranking(usuario, idEmpresa);
    }

    @GetMapping("/metricas")
    public MetricasEmpresaResponse metricas(@RequestAttribute(UsuarioAutenticado.ATRIBUTO) UsuarioAutenticado usuario,
                                            @PathVariable int idEmpresa) {
        return engajamentoBO.metricas(usuario, idEmpresa);
    }
}
