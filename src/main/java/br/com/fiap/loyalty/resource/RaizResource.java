package br.com.fiap.loyalty.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Rota pública para conferir se a API está no ar (usada no health check do deploy)
@RestController
public class RaizResource {

    @GetMapping("/")
    public Map<String, String> status() {
        return Map.of(
                "nome", "Loyalty Platform API",
                "versao", "1.0.0",
                "status", "UP");
    }
}
