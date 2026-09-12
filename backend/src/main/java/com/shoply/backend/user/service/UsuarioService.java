package com.shoply.backend.user.service;

import java.util.Locale;
import java.util.UUID;

import com.shoply.backend.security.TokenService;
import com.shoply.backend.user.dto.LoginResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoply.backend.common.exception.EmailJaCadastradoException;
import com.shoply.backend.common.exception.UsuarioNaoEncontradoException;
import com.shoply.backend.user.dto.CadastroUsuarioRequest;
import com.shoply.backend.user.dto.UsuarioResponse;
import com.shoply.backend.user.model.Usuario;
import com.shoply.backend.user.repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UsuarioService(
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

    private Usuario buscarEntidade(UUID id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
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

    @Transactional
    public UsuarioResponse cadastrar(CadastroUsuarioRequest request) {
        String nome = request.nome().trim();
        String email = normalizarEmail(request.email());

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailJaCadastradoException();
        }

        String senhaHash = passwordEncoder.encode(request.senha());

        Usuario usuario = new Usuario(nome, email, senhaHash);
        Usuario salvo = usuarioRepository.save(usuario);

        return converterParaResponse(salvo);
    }

    @Transactional
    public LoginResponse tornarVendedor(UUID id) {
        Usuario usuario = buscarEntidade(id);
        usuario.tornarVendedor();
        String accessToken = tokenService.gerarToken(usuario);

        return new LoginResponse(
                accessToken,
                "Bearer",
                tokenService.getExpiracaoEmSegundos(),
                converterParaResponse(usuario)
        );
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(UUID id) {
        return converterParaResponse(buscarEntidade(id));
    }
}
