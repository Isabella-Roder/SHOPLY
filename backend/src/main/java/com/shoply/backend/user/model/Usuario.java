package com.shoply.backend.user.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(nullable = false, length = 255)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilUsuario perfil = PerfilUsuario.CLIENTE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusUsuario status = StatusUsuario.PENDENTE;

    @Column(nullable = false)
    private boolean emailVerificado = false;

    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(nullable = false)
    private Instant atualizadoEm;

    public Usuario() {

    }

    public Usuario(
        String nome,
        String email,
        String senhaHash
    ) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.perfil = PerfilUsuario.CLIENTE;
        this.status = StatusUsuario.PENDENTE;
        this.emailVerificado = false;
    }

    @PrePersist
    private void antesDeSalvar() {
        Instant agora = Instant.now();

        criadoEm = agora;
        atualizadoEm = agora;
    }

    @PreUpdate
    private void antesDeAtualizar() {
        atualizadoEm = Instant.now();
    }

    public void verificarEmail() {
        emailVerificado = true;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public StatusUsuario getStatus() {
        return status;
    }

    public boolean isEmailVerificado() {
        return emailVerificado;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }

    public void alterarNome(String nome) {
        this.nome = nome;
    }

    public void alterarEmail(String email) {
        this.email = email;
    }

    public void alterarSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }
}
