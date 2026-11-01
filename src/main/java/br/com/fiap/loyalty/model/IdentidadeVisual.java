package br.com.fiap.loyalty.model;

// Objeto de valor (DDD): não tem id próprio e é gravado em três colunas da TB_EMPRESA
public record IdentidadeVisual(String corPrincipal, Tipografia tipografia, FormaBotoes formaBotoes) {
}
