package com.shoply.backend.user.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        URI localizacao = URI.create("/api/usuarios/" + response.id());

        return ResponseEntity.created(localizacao).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuario(
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
