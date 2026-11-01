package br.com.fiap.loyalty.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Libera o front-end (outra origem: localhost:5173 ou Vercel) para chamar a API pelo navegador.
 * <p>
 * Roda antes de qualquer outro filtro, para que até as respostas de erro (401, 404...) levem os
 * cabeçalhos de CORS; sem eles o navegador esconde a resposta e o front só vê "falha de rede".
 * As origens vêm de loyalty.cors.origens-permitidas; "*" casa com um subdomínio (ex.: https://*.vercel.app).
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter extends OncePerRequestFilter {

    private static final String METODOS = "GET, POST, PUT, PATCH, DELETE, OPTIONS";
    private static final String CABECALHOS = "Content-Type, Authorization";
    private static final String CACHE_PREFLIGHT_SEGUNDOS = "3600";

    private final List<Pattern> origensPermitidas;

    public CorsFilter(@Value("${loyalty.cors.origens-permitidas}") String origens) {
        this.origensPermitidas = Arrays.stream(origens.split(","))
                .map(String::trim)
                .filter(origem -> !origem.isEmpty())
                .map(CorsFilter::paraRegex)
                .toList();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String origem = request.getHeader(HttpHeaders.ORIGIN);
        boolean permitida = origem != null && origemPermitida(origem);

        if (permitida) {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origem);
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, METODOS);
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, CABECALHOS);
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION);
            response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, CACHE_PREFLIGHT_SEGUNDOS);
        }
        // A resposta muda conforme a origem: caches intermediários precisam saber disso
        response.addHeader(HttpHeaders.VARY, HttpHeaders.ORIGIN);

        // Preflight: o navegador pergunta antes de mandar PUT/PATCH/DELETE ou o cabeçalho Authorization
        if (ehPreflight(request)) {
            response.setStatus(permitida ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean origemPermitida(String origem) {
        return origensPermitidas.stream().anyMatch(padrao -> padrao.matcher(origem).matches());
    }

    private static boolean ehPreflight(HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod())
                && request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD) != null;
    }

    // "https://*.vercel.app" -> https://[a-z0-9-]+\.vercel\.app (o resto é comparado literalmente)
    private static Pattern paraRegex(String origem) {
        String[] partes = origem.split("\\*", -1);
        StringBuilder regex = new StringBuilder();
        for (int i = 0; i < partes.length; i++) {
            if (i > 0) {
                regex.append("[a-z0-9-]+");
            }
            regex.append(Pattern.quote(partes[i]));
        }
        return Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE);
    }
}
