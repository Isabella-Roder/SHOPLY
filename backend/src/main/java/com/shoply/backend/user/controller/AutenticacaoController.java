package com.shoply.backend.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoply.backend.user.dto.LoginRequest;
import com.shoply.backend.user.dto.LoginResponse;
import com.shoply.backend.user.service.AutenticacaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/autenticacao")
public class AutenticacaoController {
    
    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(
        AutenticacaoService autenticacaoService
    ) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(autenticacaoService.autenticar(request));
    }
}
