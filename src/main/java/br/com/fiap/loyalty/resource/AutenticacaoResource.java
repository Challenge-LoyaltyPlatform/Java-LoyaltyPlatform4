package br.com.fiap.loyalty.resource;

import br.com.fiap.loyalty.bo.AutenticacaoBO;
import br.com.fiap.loyalty.dto.LoginRequest;
import br.com.fiap.loyalty.dto.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoResource {

    private final AutenticacaoBO autenticacaoBO;

    public AutenticacaoResource(AutenticacaoBO autenticacaoBO) {
        this.autenticacaoBO = autenticacaoBO;
    }

    // 200 com token | 400 campos vazios | 401 e-mail ou senha incorretos | 403 empresa inativa
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest dados) {
        return autenticacaoBO.login(dados);
    }
}
