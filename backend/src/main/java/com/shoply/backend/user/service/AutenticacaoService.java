package com.shoply.backend.user.service;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoply.backend.common.exception.CredenciaisInvalidasException;
import com.shoply.backend.security.TokenService;
import com.shoply.backend.user.dto.LoginRequest;
import com.shoply.backend.user.dto.LoginResponse;
import com.shoply.backend.user.dto.UsuarioResponse;
import com.shoply.backend.user.model.StatusUsuario;
import com.shoply.backend.user.model.Usuario;
import com.shoply.backend.user.repository.UsuarioRepository;

@Service
public class AutenticacaoService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AutenticacaoService(
        UsuarioRepository usuarioRepository,
        PasswordEncoder passwordEncoder,
        TokenService tokenService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private UsuarioResponse converterParaResponse(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getPerfil(),
            usuario.getStatus(),
            usuario.isEmailVerificado(),
            usuario.getCriadoEm(),
            usuario.getAtualizadoEm()
        );
    }

    @Transactional(readOnly = true)
    public LoginResponse autenticar(LoginRequest request) {
        String email = normalizarEmail(request.email());

        Usuario usuario = usuarioRepository
            .findByEmailIgnoreCase(email)
            .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new CredenciaisInvalidasException();
        }

        if (usuario.getStatus() == StatusUsuario.BLOQUEADO || usuario.getStatus() == StatusUsuario.DESATIVADO) {
            throw new CredenciaisInvalidasException();
        }

        String accessToken = tokenService.gerarToken(usuario);

        return new LoginResponse(
            accessToken,
            "Bearer",
            tokenService.getExpiracaoEmSegundos(),
            converterParaResponse(usuario)
        );
    }
}
