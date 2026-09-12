package com.shoply.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoply.backend.common.exception.AcessoNegadoException;
import com.shoply.backend.common.exception.UsuarioNaoEncontradoException;
import com.shoply.backend.dtos.CadastroProdutoRequest;
import com.shoply.backend.dtos.ProdutoResponse;
import com.shoply.backend.enums.StatusProduto;
import com.shoply.backend.models.Produto;
import com.shoply.backend.repository.ProdutoRepository;
import com.shoply.backend.user.model.Usuario;
import com.shoply.backend.user.repository.UsuarioRepository;

@Service 
public class ProdutoService {
    
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProdutoService(
        ProdutoRepository produtoRepository,
        UsuarioRepository usuarioRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Produto buscarEntidade(UUID id) {
        return produtoRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    private Usuario buscarVendedor(UUID vendedorId) {
        return usuarioRepository.findById(vendedorId)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(vendedorId));
    }

    private void validarDono(Produto produto, UUID vendedorId) {
        if (!produto.getVendedor().getId().equals(vendedorId)) {
            throw new AcessoNegadoException();
        }
    }

    @Transactional 
    public ProdutoResponse cadastrar(UUID vendedorId, CadastroProdutoRequest request) {
        Usuario vendedor = buscarVendedor(vendedorId);

        Produto produto = new Produto(
            request.nome().trim(),
            request.descricao(),
            request.preco(),
            request.estoque(),
            request.categoria(),
            vendedor,
            request.imagemUrl()
        );

        return ProdutoResponse.from(produtoRepository.save(produto));
    }

    @Transactional
    public ProdutoResponse atualizar(UUID vendedorId, UUID id, CadastroProdutoRequest request) {
        buscarVendedor(vendedorId);
        Produto produto = buscarEntidade(id);
        validarDono(produto, vendedorId);

        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setPreco(request.preco());
        produto.setEstoque(request.estoque());
        produto.setCategoria(request.categoria());
        produto.setImagemUrl(request.imagemUrl());

        return ProdutoResponse.from(produtoRepository.save(produto));
    }

    @Transactional
    public void deletar(UUID vendedorId, UUID id) {
        Produto produto = buscarEntidade(id);
        validarDono(produto, vendedorId);
        produtoRepository.delete(produto);
    }

    @Transactional
    public void desativar(UUID vendedorId, UUID id) {
        Produto produto = buscarEntidade(id);
        validarDono(produto, vendedorId);
        produto.desativar();
    }

    @Transactional
    public void ativar(UUID vendedorId, UUID id) {
        Produto produto = buscarEntidade(id);
        validarDono(produto, vendedorId);
        produto.ativar();
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(UUID id) {
        return ProdutoResponse.from(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarAtivos() {
        return produtoRepository.findByStatus(StatusProduto.ATIVO)
            .stream().map(ProdutoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarPorVendedor(UUID vendedorId) {
        return produtoRepository.findByVendedorId(vendedorId)
            .stream().map(ProdutoResponse::from).toList();
    }
}
