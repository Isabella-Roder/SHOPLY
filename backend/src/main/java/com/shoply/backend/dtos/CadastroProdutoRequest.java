package com.shoply.backend.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastroProdutoRequest(

    @NotBlank(message = "Nome é obrigatório")
    @Size(
        min = 2,
        max = 150,
        message = "Nome deve ter entre 2 e 150 caracteres"
    )
    String nome,

    @Size(
        max = 500,
        message = "Descrição deve ter no máximo 500 caracteres"
    )
    String descricao,

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero.")
    BigDecimal preco,

    @NotNull(message = "Estoque é obrigatório")
    @Min(value = 0, message = "Estoque não pode ser negativo")
    Integer estoque,

    @Size(
        max = 100,
        message = "Categoria deve ter no máximo 100 caracteres"
    )
    String categoria,

    @Size(
        max = 500,
        message = "Url da imagem deve ter no máximo 500 caracteres"
    )
    String imagemUrl
) {
    
}
