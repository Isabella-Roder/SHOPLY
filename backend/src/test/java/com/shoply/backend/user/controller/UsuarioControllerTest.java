package com.shoply.backend.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.shoply.backend.common.exception.EmailJaCadastradoException;
import com.shoply.backend.common.exception.GlobalExceptionHandler;
import com.shoply.backend.config.SegurancaConfig;
import com.shoply.backend.user.dto.CadastroUsuarioRequest;
import com.shoply.backend.user.dto.UsuarioResponse;
import com.shoply.backend.user.model.PerfilUsuario;
import com.shoply.backend.user.model.StatusUsuario;
import com.shoply.backend.user.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
@Import({
    SegurancaConfig.class,
    GlobalExceptionHandler.class
})
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    void deveCadastrarUsuarioERetornar201() throws Exception {
        UUID id = UUID.randomUUID();
        Instant agora = Instant.now();

        UsuarioResponse response = new UsuarioResponse(
            id,
            "Maria Silva",
            "maria@exemplo.com",
            PerfilUsuario.CLIENTE,
            StatusUsuario.PENDENTE,
            false,
            agora,
            agora
        );

        when(usuarioService.cadastrar(any(CadastroUsuarioRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "nome": "Maria Silva",
                        "email": "maria@exemplo.com",
                        "senha": "senha-segura"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(header().string(
                "Location",
                "/api/usuarios/" + id
            ))
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.nome").value("Maria Silva"))
            .andExpect(jsonPath("$.email").value("maria@exemplo.com"))
            .andExpect(jsonPath("$.senhaHash").doesNotExist());
    }

    @Test
    void deveRetornar400QuandoCadastroForInvalido() throws Exception {
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "nome": "",
                        "email": "email-invalido",
                        "senha": "123"
                    }
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.mensagem")
                .value("Dados de entrada inválidos"))
            .andExpect(jsonPath("$.campos.nome").exists())
            .andExpect(jsonPath("$.campos.email").exists())
            .andExpect(jsonPath("$.campos.senha").exists());

        verify(usuarioService, never())
            .cadastrar(any(CadastroUsuarioRequest.class));
    }

    @Test
    void deveRetornar409QuandoEmailJaEstiverCadastrado()
        throws Exception {

        when(usuarioService.cadastrar(any(CadastroUsuarioRequest.class)))
            .thenThrow(new EmailJaCadastradoException());

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "nome": "Maria Silva",
                        "email": "maria@exemplo.com",
                        "senha": "senha-segura"
                    }
                    """))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.mensagem")
                .value("E-mail já cadastrado"))
            .andExpect(jsonPath("$.caminho")
                .value("/api/usuarios"));
    }

    @Test
    void deveImpedirBuscaSemAutenticacao() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/api/usuarios/{id}", id))
            .andExpect(status().isUnauthorized());

        verify(usuarioService, never()).buscarPorId(any(UUID.class));
    }
}