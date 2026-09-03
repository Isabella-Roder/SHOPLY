package com.shoply.backend.user.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoply.backend.user.dto.CadastroUsuarioRequest;
import com.shoply.backend.user.dto.UsuarioResponse;
import com.shoply.backend.user.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(
        @Valid @RequestBody CadastroUsuarioRequest request 
    ) {
        UsuarioResponse response = service.cadastrar(request);

        URI localizacao = URI.create("/api/usuarios/me");

        return ResponseEntity.created(localizacao).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> buscarUsuarioAutenticado(
        @AuthenticationPrincipal Jwt jwt
    ) {
        UUID usuarioId = UUID.fromString(jwt.getSubject());

        return ResponseEntity.ok(service.buscarPorId(usuarioId));
    }
}
