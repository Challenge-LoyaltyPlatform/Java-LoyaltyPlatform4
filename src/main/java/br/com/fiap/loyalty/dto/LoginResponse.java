package br.com.fiap.loyalty.dto;

// Resposta do login: token para o cabeçalho Authorization e os dados de quem entrou
public record LoginResponse(String token, UsuarioLogadoResponse usuario) {
}
