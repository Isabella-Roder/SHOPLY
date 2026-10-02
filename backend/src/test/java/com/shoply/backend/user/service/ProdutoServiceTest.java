package com.shoply.backend.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.shoply.backend.common.exception.AcessoNegadoException;
import com.shoply.backend.common.exception.UsuarioNaoEncontradoException;
import com.shoply.backend.dtos.CadastroProdutoRequest;
import com.shoply.backend.dtos.ProdutoResponse;
import com.shoply.backend.enums.StatusProduto;
import com.shoply.backend.models.Produto;
import com.shoply.backend.repository.ProdutoRepository;
import com.shoply.backend.service.ProdutoService;
import com.shoply.backend.user.model.Usuario;
import com.shoply.backend.user.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {
    
    @Mock 
    private ProdutoRepository produtoRepository;

    @Mock 
    private UsuarioRepository usuarioRepository;

    private ProdutoService produtoService;

    private UUID vendedorId;
    private Usuario vendedor;

    @BeforeEach 
    void configurar() {
        produtoService = new ProdutoService(produtoRepository, usuarioRepository);

        vendedorId = UUID.randomUUID();
        vendedor = new Usuario("Vendedor Teste", "vendedor@exemplo.com", "hash");
        ReflectionTestUtils.setField(vendedor, "id", vendedorId);
    }

    private Produto criarProdutoExistente(UUID id, Usuario donoDoProduto) {
        Produto produto = new Produto(
            "Produto existente",
            "Descrição",
            new BigDecimal("50.00"),
            5,
            "Categoria",
            donoDoProduto,
            null
        );

        ReflectionTestUtils.setField(produto, "id", id);
        return produto;
    }

    private CadastroProdutoRequest criarRequest() {
        return new CadastroProdutoRequest(
            " Tênis Urban Runner ",
            "Descrição do produto",
            new BigDecimal("259.90"),
            10,
            "Calçados",
            null
        );
    }

    @Test 
    void deveCadastrarProdutoVinculadoAoVendedorAutenticado() {
        when(usuarioRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.save(any(Produto.class)))
            .thenAnswer(invocar -> invocar.getArgument(0));

        ProdutoResponse response = produtoService.cadastrar(vendedorId, criarRequest());

        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoRepository).save(captor.capture());

        Produto produtoSalvo = captor.getValue();

        assertEquals("Tênis Urban Runner", produtoSalvo.getNome());
        assertEquals(StatusProduto.ATIVO, produtoSalvo.getStatus());
        assertEquals(vendedorId, response.vendedorId());

    }

    @Test 
    void naoDeveCadastrarQuandoVendedorNaoExistir() {
        when(usuarioRepository.findById(vendedorId)).thenReturn(Optional.empty());

        assertThrows(
            UsuarioNaoEncontradoException.class,
            () -> produtoService.cadastrar(vendedorId, criarRequest())
        );
        
        verify(produtoRepository, never()).save(any());
    }

    @Test 
    void deveAtualizarProdutoQuandoVendedorForDono() {
        UUID produtoId = UUID.randomUUID();
        Produto produto = criarProdutoExistente(produtoId, vendedor);

        when(usuarioRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class)))
            .thenAnswer(invocacao -> invocacao.getArgument(0));

        ProdutoResponse response = produtoService.atualizar(vendedorId, produtoId, criarRequest());

        assertEquals("Tênis Urban Runner", response.nome());
    }

    @Test 
    void naoDeveAtualizarQuandoVendedorNaoForDonoDoProduto() {
        UUID produtoId = UUID.randomUUID();
        Usuario outroVendedor = new Usuario("Outro", "outro@exemplo.com", "hash");
        ReflectionTestUtils.setField(outroVendedor, "id", UUID.randomUUID());

        Produto produto = criarProdutoExistente(produtoId, outroVendedor);

        when(usuarioRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

        assertThrows(
            AcessoNegadoException.class,
            () -> produtoService.atualizar(vendedorId, produtoId, criarRequest())
        );

        verify(produtoRepository, never()).save(any());
    }

    @Test 
    void deveDesativarProdutoAtivoQuandoVendedorForDono() {
        UUID produtoId = UUID.randomUUID();
        Produto produto = criarProdutoExistente(produtoId, vendedor);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

        produtoService.desativar(vendedorId, produtoId);

        assertEquals(StatusProduto.INATIVO, produto.getStatus());
    }

    @Test 
    void naoDeveDesativarQuandoVendedorNaoForDono() {
        UUID produtoId = UUID.randomUUID();
        Usuario outroVendedor = new Usuario("Outro", "outro@exemplo.com", "hash");
        ReflectionTestUtils.setField(outroVendedor, "id", UUID.randomUUID());

        Produto produto = criarProdutoExistente(produtoId, outroVendedor);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

        assertThrows(
            AcessoNegadoException.class, 
            () -> produtoService.desativar(vendedorId, produtoId)
        );

        assertEquals(StatusProduto.ATIVO, produto.getStatus());
    }

    @Test 
    void deveExcluirProdutoQuandoVendedorForDono() {
        UUID produtoId = UUID.randomUUID();
        Produto produto = criarProdutoExistente(produtoId, vendedor);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

        produtoService.deletar(vendedorId, produtoId);

        verify(produtoRepository, times(1)).delete(produto);
    }

    @Test 
    void naoDeveExcluirQuandoVendedorNaoForDono() {
        UUID produtoId = UUID.randomUUID();
        Usuario outroVendedor = new Usuario("Outro", "outro@exemplo.com", "hash");
        ReflectionTestUtils.setField(outroVendedor, "id", UUID.randomUUID());

        Produto produto = criarProdutoExistente(produtoId, outroVendedor);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

        assertThrows(
            AcessoNegadoException.class, 
            () -> produtoService.deletar(vendedorId, produtoId)
        );

        verify(produtoRepository, never()).delete(any());
    }

    @Test
    void deveListarApenasProdutosAtivos() {
        Produto produtoAtivo = criarProdutoExistente(UUID.randomUUID(), vendedor);

        when(produtoRepository.findByStatus(StatusProduto.ATIVO))
            .thenReturn(List.of(produtoAtivo));

        List<ProdutoResponse> produtos = produtoService.listarAtivos();

        assertEquals(1, produtos.size());
        verify(produtoRepository).findByStatus(StatusProduto.ATIVO);
    }
}
