package com.shoply.backend.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shoply.backend.common.exception.CredenciaisInvalidasException;
import com.shoply.backend.security.TokenService;
import com.shoply.backend.user.dto.LoginRequest;
import com.shoply.backend.user.dto.LoginResponse;
import com.shoply.backend.user.model.Usuario;
import com.shoply.backend.user.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class AutenticacaoServiceTest {
    
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    private AutenticacaoService autenticacaoService;

    @BeforeEach
    void configurar() {
        autenticacaoService = new AutenticacaoService(usuarioRepository, passwordEncoder, tokenService);
    }

    @Test
    void deveAutenticarComEmailESenhaValidos() {
        LoginRequest request = new LoginRequest("maria@exemplo.com", "senha-segura");

        Usuario usuario = new Usuario(
            "Maria Silva",
            "maria@exemplo.com",
            "senha-hash"
        );

        when(usuarioRepository.findByEmailIgnoreCase("maria@exemplo.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha-segura", "senha-hash")).thenReturn(true);
        when(tokenService.gerarToken(usuario)).thenReturn("token-assinado");
        when(tokenService.getExpiracaoEmSegundos()).thenReturn(900L);

        LoginResponse response = autenticacaoService.autenticar(request);

        assertEquals("token-assinado", response.accessToken());
        assertEquals("Bearer", response.tipo());
        assertEquals(900L, response.expiraEmSegundos());
        assertEquals("Maria Silva", response.usuario().nome());
        assertEquals("maria@exemplo.com", response.usuario().email());
    }

    @Test
    void deveRecusarEmailInexistente() {
        LoginRequest request = new LoginRequest("inexistente@exemplo.com", "senha-segura");

        when(usuarioRepository.findByEmailIgnoreCase("inexistente@exemplo.com")).thenReturn(Optional.empty());

        assertThrows(CredenciaisInvalidasException.class, () -> autenticacaoService.autenticar(request));

        verify(passwordEncoder, never()).matches(any(), any());
        verify(tokenService, never()).gerarToken(any());
    }

    @Test
    void deveRecusarSenhaIncorreta() {
        LoginRequest request = new LoginRequest("maria@exemplo.com", "senha-incorreta");

        Usuario usuario = new Usuario(
            "Maria Silva",
            "maria@exemplo.com",
            "senha-hash"
        );

        when(usuarioRepository.findByEmailIgnoreCase("maria@exemplo.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha-incorreta", "senha-hash")).thenReturn(false);

        assertThrows(CredenciaisInvalidasException.class, () -> autenticacaoService.autenticar(request));

        verify(tokenService, never()).gerarToken(any());
    }
}
