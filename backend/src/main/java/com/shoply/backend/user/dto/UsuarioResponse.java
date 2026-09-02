package com.shoply.backend.user.dto;

import java.time.Instant;
import java.util.UUID;

import com.shoply.backend.user.model.PerfilUsuario;
import com.shoply.backend.user.model.StatusUsuario;

public record UsuarioResponse(
    UUID id,
    String nome,
    String email,
    PerfilUsuario perfil,
    StatusUsuario status,
    boolean emailVerificado,
    Instant criadoEm,
    Instant atualizadoEm
) {
}
