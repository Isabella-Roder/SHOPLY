package com.shoply.backend.user.dto;

public record LoginResponse(
    String accessToken,
    String tipo,
    long expiraEmSegundos,
    UsuarioResponse usuario
) {
    
}
