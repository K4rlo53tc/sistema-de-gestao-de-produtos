package CJ_DEV.Sistema.de.Gestao.de.Produtos.service;

import CJ_DEV.Sistema.de.Gestao.de.Produtos.dto.ProdutoRequest;
import CJ_DEV.Sistema.de.Gestao.de.Produtos.dto.ProdutoResponse;
import CJ_DEV.Sistema.de.Gestao.de.Produtos.exception.ProdutoNaoEncontradoException;
import CJ_DEV.Sistema.de.Gestao.de.Produtos.model.Produto;
import CJ_DEV.Sistema.de.Gestao.de.Produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void buscarPorId_quandoExiste_devolveProduto() {
        // Arrange: preparar os dados e o que o mock responde
        Produto produto = Produto.builder()
                .id(1L)
                .nome("Teclado")
                .preco(new BigDecimal("25000.00"))
                .quantidade(10)
                .categoria("Periféricos")
                .build();
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        // Act: chamar o método que queremos testar
        ProdutoResponse resposta = produtoService.buscarPorId(1L);

        // Assert: verificar o resultado
        assertEquals(1L, resposta.id());
        assertEquals("Teclado", resposta.nome());
    }

    @Test
    void buscarPorId_quandoNaoExiste_lancaExcecao() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ProdutoNaoEncontradoException.class, () -> produtoService.buscarPorId(99L));
    }

    @Test
    void cadastrarProduto_removeEspacosDoNome() {
        ProdutoRequest request = new ProdutoRequest(
                "  Teclado  ", null, new BigDecimal("35000.00"), 15, "  Periféricos  ");
        when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

        ProdutoResponse resposta = produtoService.cadastrarProduto(request);

        assertEquals("Teclado", resposta.nome());
        assertEquals("Periféricos", resposta.categoria());
    }

    @Test
    void excluirProduto_quandoNaoExiste_lancaExcecao() {

        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProdutoNaoEncontradoException.class, () -> produtoService.excluirProduto(99L));

        verify(produtoRepository, never()).delete(any());
    }

}