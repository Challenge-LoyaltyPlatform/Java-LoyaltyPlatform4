package br.com.fiap.loyalty.model;

public enum StatusSequencia {
    ATIVA,      // usuário interagiu dentro da janela de 24h
    SUSPENSA,   // passou de 24h — restaurável automaticamente (RN13)
    ZERADA      // passou de 48h — contagem reiniciada (RN14)
}
