package br.com.fiap.loyalty.filter;

import br.com.fiap.loyalty.dto.ErroResponse;
import br.com.fiap.loyalty.exception.NaoAutorizadoException;
import br.com.fiap.loyalty.security.TokenService;
import br.com.fiap.loyalty.security.UsuarioAutenticado;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * Exige "Authorization: Bearer &lt;token&gt;" em toda rota que não é pública.
 * Com token válido, guarda o {@link UsuarioAutenticado} na requisição para os resources;
 * sem token ou com token expirado, responde 401 (o front desloga e manda para o login).
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class AutenticacaoFilter extends OncePerRequestFilter {

    private static final String PREFIXO_BEARER = "Bearer ";
    private static final Set<String> ROTAS_PUBLICAS = Set.of("/", "/auth/login", "/error", "/favicon.ico");

    private final TokenService tokenService;
    private final JsonMapper jsonMapper;

    public AutenticacaoFilter(TokenService tokenService, JsonMapper jsonMapper) {
        this.tokenService = tokenService;
        this.jsonMapper = jsonMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String caminho = request.getRequestURI().substring(request.getContextPath().length());
        return "OPTIONS".equalsIgnoreCase(request.getMethod()) || ROTAS_PUBLICAS.contains(caminho);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String cabecalho = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (cabecalho == null || !cabecalho.regionMatches(true, 0, PREFIXO_BEARER, 0, PREFIXO_BEARER.length())) {
            responderNaoAutorizado(response, "Faça login para continuar.");
            return;
        }

        try {
            UsuarioAutenticado usuario = tokenService.validar(cabecalho.substring(PREFIXO_BEARER.length()).trim());
            request.setAttribute(UsuarioAutenticado.ATRIBUTO, usuario);
        } catch (NaoAutorizadoException e) {
            responderNaoAutorizado(response, e.getMessage());
            return;
        }

        chain.doFilter(request, response);
    }

    // Mesmo formato de erro do GlobalExceptionHandler (o filtro roda antes dos controllers)
    private void responderNaoAutorizado(HttpServletResponse response, String mensagem) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(jsonMapper.writeValueAsString(new ErroResponse(401, mensagem)));
    }
}
