package com.shoply.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoply.backend.enums.StatusProduto;
import com.shoply.backend.models.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
    
    List<Produto> findByVendedorId(UUID vendedorId);

    List<Produto> findByStatus(StatusProduto status);

}
