package com.shoply.backend.models;

import java.util.UUID;

import com.shoply.backend.enums.StatusProduto;
import com.shoply.backend.user.model.Usuario;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(
    name = "produtos"
)
public class Produto {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer estoque;

    @Column(length = 100)
    private String categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProduto status = StatusProduto.ATIVO;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "vendedor_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_produtos_vendedor")
    )
    private Usuario vendedor;

    @Column(length = 500)
    private String imagemUrl;

    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(nullable = false)
    private Instant atualizadoEm;

    public Produto() {

    }

    public Produto(
        String nome,
        String descricao,
        BigDecimal preco,
        Integer estoque, 
        String categoria,
        Usuario vendedor,
        String imagemUrl
    ) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = categoria;
        this.vendedor = vendedor;
        this.imagemUrl = imagemUrl;
        this.status = StatusProduto.ATIVO;
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

    public void desativar() {
        if (status != StatusProduto.ATIVO) {
            throw new IllegalArgumentException("Produto precisa estar ativo para desativar.");
        }

        status = StatusProduto.INATIVO;
    }

    public void ativar() {
        if (status != StatusProduto.INATIVO) {
            throw new IllegalArgumentException("Produto precisa estar desativado para ativar.");
        }

        status = StatusProduto.ATIVO;
    }

    public void esgotado() {
        if (estoque <= 0) {
            status = StatusProduto.ESGOTADO;
        }
    }

    public void validarValor() {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero.");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public String getCategoria() {
        return categoria;
    }

    public StatusProduto getStatus() {
        return status;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}
