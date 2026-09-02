package com.shoply.backend.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shoply.backend.common.exception.EmailJaCadastradoException;
import com.shoply.backend.user.dto.CadastroUsuarioRequest;
import com.shoply.backend.user.dto.UsuarioResponse;
import com.shoply.backend.user.model.PerfilUsuario;
import com.shoply.backend.user.model.StatusUsuario;
import com.shoply.backend.user.model.Usuario;
import com.shoply.backend.user.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UsuarioService usuarioService;

    @BeforeEach
    void configurar() {
        usuarioService = new UsuarioService(
            usuarioRepository,
            passwordEncoder
        );
    }

    @Test
    void deveCadastrarUsuarioComDadosNormalizadosESenhaProtegida() {
        CadastroUsuarioRequest request = new CadastroUsuarioRequest(
            "  Maria Silva  ",
            "  MARIA@EXEMPLO.COM  ",
            "senha-segura"
        );

        when(usuarioRepository.existsByEmailIgnoreCase("maria@exemplo.com"))
            .thenReturn(false);

        when(passwordEncoder.encode("senha-segura"))
            .thenReturn("senha-protegida");

        when(usuarioRepository.save(any(Usuario.class)))
            .thenAnswer(invocacao -> invocacao.getArgument(0));

        UsuarioResponse response = usuarioService.cadastrar(request);

        ArgumentCaptor<Usuario> captor =
            ArgumentCaptor.forClass(Usuario.class);

        verify(usuarioRepository).save(captor.capture());

        Usuario usuarioSalvo = captor.getValue();

        assertEquals("Maria Silva", usuarioSalvo.getNome());
        assertEquals("maria@exemplo.com", usuarioSalvo.getEmail());
        assertEquals("senha-protegida", usuarioSalvo.getSenhaHash());
        assertEquals(PerfilUsuario.CLIENTE, usuarioSalvo.getPerfil());
        assertEquals(StatusUsuario.PENDENTE, usuarioSalvo.getStatus());
        assertTrue(!usuarioSalvo.isEmailVerificado());

        assertEquals("Maria Silva", response.nome());
        assertEquals("maria@exemplo.com", response.email());
    }

    @Test
    void naoDeveCadastrarQuandoEmailJaExistir() {
        CadastroUsuarioRequest request = new CadastroUsuarioRequest(
            "Maria Silva",
            "maria@exemplo.com",
            "senha-segura"
        );

        when(usuarioRepository.existsByEmailIgnoreCase("maria@exemplo.com"))
            .thenReturn(true);

        assertThrows(
            EmailJaCadastradoException.class,
            () -> usuarioService.cadastrar(request)
        );

        verify(passwordEncoder, never()).encode(any());
        verify(usuarioRepository, never()).save(any());
    }
}