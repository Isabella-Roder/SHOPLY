package com.shoply.backend.controller;


import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoply.backend.dtos.CadastroProdutoRequest;
import com.shoply.backend.dtos.ProdutoResponse;
import com.shoply.backend.service.ProdutoService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/produtos")
public class ProdutoController {
    
    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<ProdutoResponse> cadastrar(
        @AuthenticationPrincipal  Jwt jwt,
        @Valid @RequestBody CadastroProdutoRequest request
    ) {
        UUID vendedorId = UUID.fromString(jwt.getSubject());
        ProdutoResponse response = service.cadastrar(vendedorId, request);

        URI localizacao = URI.create("/api/produtos/" + response.id());

        return ResponseEntity.created(localizacao).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<ProdutoResponse> atualizar(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID id,
        @Valid @RequestBody CadastroProdutoRequest request
    ) {
        UUID vendedorId = UUID.fromString(jwt.getSubject());

        return ResponseEntity.ok(service.atualizar(vendedorId, id, request));
    }

    @PatchMapping("/{id}/desativar")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<Void> desativar(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID vendedorId = UUID.fromString(jwt.getSubject());
        service.desativar(vendedorId, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<Void> ativar(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID vendedorId = UUID.fromString(jwt.getSubject());
        service.ativar(vendedorId, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<Void> deletar(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID vendedorId = UUID.fromString(jwt.getSubject());
        service.deletar(vendedorId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping 
    public ResponseEntity<List<ProdutoResponse>> listarAtivos() {
        return ResponseEntity.ok(service.listarAtivos());
    }

    @GetMapping("/meus")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<List<ProdutoResponse>> listarMeusProdutos(@AuthenticationPrincipal Jwt jwt) {
        UUID vendedorId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(service.listarPorVendedor(vendedorId));
    }


}
