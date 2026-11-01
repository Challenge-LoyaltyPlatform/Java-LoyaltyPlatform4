package br.com.fiap.loyalty.exception;

import br.com.fiap.loyalty.dto.ErroResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.MismatchedInputException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Converte toda exceção em uma resposta JSON no formato { status, mensagem, campos? },
 * com o código HTTP certo e sem expor stack trace para o cliente.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String DADOS_INVALIDOS = "Os dados enviados são inválidos. Confira os campos destacados.";

    // 400 — falha no Bean Validation dos DTOs (@NotBlank, @Pattern...): uma mensagem por campo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validacao(MethodArgumentNotValidException e) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError erro : e.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(erro.getField(), erro.getDefaultMessage());
        }
        return responder(HttpStatus.BAD_REQUEST, DADOS_INVALIDOS, campos);
    }

    // 400 — validação feita no BO (ex.: data de fim antes da de início)
    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<ErroResponse> dadosInvalidos(DadosInvalidosException e) {
        return responder(HttpStatus.BAD_REQUEST, e.getMessage(), e.getCampos());
    }

    // 400 — JSON malformado ou valor de tipo errado (ex.: "tipografia": "ARIAL", data fora do formato)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> corpoIlegivel(HttpMessageNotReadableException e) {
        MismatchedInputException erroDeCampo = procurarCausa(e, MismatchedInputException.class);
        if (erroDeCampo != null && !erroDeCampo.getPath().isEmpty()) {
            String campo = erroDeCampo.getPath().stream()
                    .map(JacksonException.Reference::getPropertyName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("."));
            return responder(HttpStatus.BAD_REQUEST, DADOS_INVALIDOS,
                    Map.of(campo, mensagemDeTipo(erroDeCampo.getTargetType())));
        }
        return responder(HttpStatus.BAD_REQUEST, "O corpo da requisição está vazio ou não é um JSON válido.");
    }

    // 400 — parâmetro da URL com tipo errado (ex.: /empresas/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> parametroInvalido(MethodArgumentTypeMismatchException e) {
        return responder(HttpStatus.BAD_REQUEST, "O parâmetro '" + e.getName() + "' deve ser um número inteiro.");
    }

    // 401 — login inválido (o token ausente/expirado é tratado no AutenticacaoFilter)
    @ExceptionHandler(NaoAutorizadoException.class)
    public ResponseEntity<ErroResponse> naoAutorizado(NaoAutorizadoException e) {
        return responder(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    // 403 — logado, mas sem permissão (ex.: perfil EMPRESA em rota de administrador)
    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ErroResponse> acessoNegado(AcessoNegadoException e) {
        return responder(HttpStatus.FORBIDDEN, e.getMessage());
    }

    // 404 — registro não existe
    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<ErroResponse> naoEncontrado(NaoEncontradoException e) {
        return responder(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // 404 — rota não existe
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResponse> rotaInexistente(NoResourceFoundException e) {
        return responder(HttpStatus.NOT_FOUND, "Rota não encontrada: /" + e.getResourcePath());
    }

    // 405 — verbo HTTP não suportado na rota
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResponse> metodoNaoSuportado(HttpRequestMethodNotSupportedException e) {
        return responder(HttpStatus.METHOD_NOT_ALLOWED, "O método " + e.getMethod() + " não é suportado nesta rota.");
    }

    // 415 — corpo enviado sem Content-Type: application/json
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErroResponse> tipoDeMidia(HttpMediaTypeNotSupportedException e) {
        return responder(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Envie o corpo em JSON (Content-Type: application/json).");
    }

    // 409 — regra de negócio ou unicidade (CNPJ/e-mail duplicado, empresa já inativa...)
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> regraNegocio(RegraNegocioException e) {
        return responder(HttpStatus.CONFLICT, e.getMessage(), e.getCampos());
    }

    // 409 se o banco recusou por constraint (garantia final contra duplicidade); 500 para o resto
    @ExceptionHandler(PersistenciaException.class)
    public ResponseEntity<ErroResponse> persistencia(PersistenciaException e) {
        if (e.violouRestricao()) {
            LOG.warn("Restrição do banco violada: {}", e.getCause().getMessage());
            return responder(HttpStatus.CONFLICT,
                    "Os dados violam uma restrição do banco (registro duplicado ou referência inválida).");
        }
        LOG.error("Falha ao acessar o banco de dados", e);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR,
                "Não foi possível acessar o banco de dados. Tente novamente em instantes.");
    }

    // 500 — qualquer outro erro: registra no log e devolve mensagem genérica
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> erroInesperado(Exception e) {
        LOG.error("Erro inesperado", e);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado no servidor.");
    }

    private static String mensagemDeTipo(Class<?> tipo) {
        if (tipo == null) {
            return "Valor inválido.";
        }
        if (tipo.isEnum()) {
            String valores = Arrays.stream(tipo.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            return "Valor inválido. Use: " + valores + ".";
        }
        if (tipo == LocalDate.class) {
            return "Data inválida. Use o formato AAAA-MM-DD.";
        }
        if (Number.class.isAssignableFrom(tipo) || tipo == int.class || tipo == long.class) {
            return "Informe um número inteiro.";
        }
        return "Valor inválido.";
    }

    private static <T extends Throwable> T procurarCausa(Throwable erro, Class<T> tipo) {
        for (Throwable atual = erro; atual != null; atual = atual.getCause()) {
            if (tipo.isInstance(atual)) {
                return tipo.cast(atual);
            }
        }
        return null;
    }

    private static ResponseEntity<ErroResponse> responder(HttpStatus status, String mensagem) {
        return responder(status, mensagem, Map.of());
    }

    private static ResponseEntity<ErroResponse> responder(HttpStatus status, String mensagem, Map<String, String> campos) {
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), mensagem, campos));
    }
}
