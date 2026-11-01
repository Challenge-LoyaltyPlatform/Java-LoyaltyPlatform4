package br.com.fiap.loyalty.security;

import br.com.fiap.loyalty.exception.NaoAutorizadoException;
import br.com.fiap.loyalty.model.AcessoPainel;
import br.com.fiap.loyalty.model.PerfilAcesso;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Gera e valida o token de acesso no formato JWT (HS256), com a API de criptografia do próprio Java.
 * O token leva quem é o usuário, o perfil, a empresa de contexto e a data de expiração,
 * assinado com a chave secreta JWT_SECRET: qualquer alteração no conteúdo invalida a assinatura.
 */
@Component
public class TokenService {

    private static final String ALGORITMO = "HmacSHA256";
    private static final int TAMANHO_MINIMO_CHAVE = 32;
    private static final Base64.Encoder BASE64 = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder BASE64_DECODER = Base64.getUrlDecoder();
    private static final String CABECALHO =
            BASE64.encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
    private static final TypeReference<Map<String, Object>> TIPO_CLAIMS = new TypeReference<>() {
    };

    private final SecretKeySpec chave;
    private final Duration validade;
    private final JsonMapper jsonMapper;
    private final Clock relogio;

    public TokenService(@Value("${loyalty.jwt.secret}") String segredo,
                        @Value("${loyalty.jwt.expiracao-horas}") long horasDeValidade,
                        JsonMapper jsonMapper,
                        Clock relogio) {
        byte[] bytesDaChave = segredo.getBytes(StandardCharsets.UTF_8);
        if (bytesDaChave.length < TAMANHO_MINIMO_CHAVE) {
            throw new IllegalStateException("JWT_SECRET precisa ter pelo menos " + TAMANHO_MINIMO_CHAVE + " caracteres.");
        }
        this.chave = new SecretKeySpec(bytesDaChave, ALGORITMO);
        this.validade = Duration.ofHours(horasDeValidade);
        this.jsonMapper = jsonMapper;
        this.relogio = relogio;
    }

    public String gerar(AcessoPainel acesso) {
        Instant agora = relogio.instant();

        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("sub", String.valueOf(acesso.getId()));
        claims.put("nome", acesso.getNome());
        claims.put("email", acesso.getEmail());
        claims.put("perfil", acesso.getPerfil().name());
        if (acesso.getIdEmpresa() != null) {
            claims.put("empresa", acesso.getIdEmpresa());
        }
        claims.put("iat", agora.getEpochSecond());
        claims.put("exp", agora.plus(validade).getEpochSecond());

        String conteudo = CABECALHO + "." + BASE64.encodeToString(jsonMapper.writeValueAsBytes(claims));
        return conteudo + "." + BASE64.encodeToString(assinar(conteudo));
    }

    public UsuarioAutenticado validar(String token) {
        String[] partes = token.split("\\.");
        // Só aceita o cabeçalho que esta API gera: impede trocar o algoritmo (ex.: "alg": "none")
        if (partes.length != 3 || !partes[0].equals(CABECALHO)) {
            throw tokenInvalido();
        }

        try {
            byte[] assinaturaRecebida = BASE64_DECODER.decode(partes[2]);
            byte[] assinaturaEsperada = assinar(partes[0] + "." + partes[1]);
            // Comparação em tempo constante, para não vazar quantos bytes conferem
            if (!MessageDigest.isEqual(assinaturaEsperada, assinaturaRecebida)) {
                throw tokenInvalido();
            }

            Map<String, Object> claims = jsonMapper.readValue(BASE64_DECODER.decode(partes[1]), TIPO_CLAIMS);

            long expiracao = ((Number) claims.get("exp")).longValue();
            if (relogio.instant().getEpochSecond() >= expiracao) {
                throw new NaoAutorizadoException("Sua sessão expirou. Entre novamente.");
            }

            Object empresa = claims.get("empresa");
            return new UsuarioAutenticado(
                    Integer.parseInt((String) claims.get("sub")),
                    (String) claims.get("nome"),
                    (String) claims.get("email"),
                    PerfilAcesso.valueOf((String) claims.get("perfil")),
                    empresa == null ? null : ((Number) empresa).intValue());
        } catch (NaoAutorizadoException e) {
            throw e;
        } catch (RuntimeException e) {
            // Base64 quebrado, JSON inválido ou claim faltando: tudo é token inválido
            throw tokenInvalido();
        }
    }

    private byte[] assinar(String conteudo) {
        try {
            // Mac não é thread-safe: uma instância por chamada
            Mac mac = Mac.getInstance(ALGORITMO);
            mac.init(chave);
            return mac.doFinal(conteudo.getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Não foi possível assinar o token.", e);
        }
    }

    private static NaoAutorizadoException tokenInvalido() {
        return new NaoAutorizadoException("Sessão inválida. Entre novamente.");
    }
}
