package com.shoply.backend.dtos;

import java.util.UUID;

import com.shoply.backend.enums.StatusProduto;
import com.shoply.backend.models.Produto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProdutoResponse(
    UUID id,
    String nome,
    String descricao,
    BigDecimal preco,
    Integer estoque,
    String categoria,
    StatusProduto status,
    UUID vendedorId,
    String imagemUrl,
    Instant criadoEm,
    Instant atualizadoEm
) {
    public static ProdutoResponse from(Produto produto) {
        return new ProdutoResponse(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getEstoque(),
            produto.getCategoria(),
            produto.getStatus(),
            produto.getVendedor().getId(),
            produto.getImagemUrl(),
            produto.getCriadoEm(),
            produto.getAtualizadoEm()
        );
    }
}
