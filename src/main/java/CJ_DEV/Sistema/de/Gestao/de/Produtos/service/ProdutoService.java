package CJ_DEV.Sistema.de.Gestao.de.Produtos.service;

import CJ_DEV.Sistema.de.Gestao.de.Produtos.dto.ProdutoPatchRequest;
import CJ_DEV.Sistema.de.Gestao.de.Produtos.dto.ProdutoRequest;
import CJ_DEV.Sistema.de.Gestao.de.Produtos.dto.ProdutoResponse;
import CJ_DEV.Sistema.de.Gestao.de.Produtos.exception.ProdutoNaoEncontradoException;
import CJ_DEV.Sistema.de.Gestao.de.Produtos.model.Produto;
import CJ_DEV.Sistema.de.Gestao.de.Produtos.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoResponse cadastrarProduto(ProdutoRequest produtoRequest){
        Produto produto = Produto.builder()
                .nome(limpar(produtoRequest.nome()))
                .descricao(limpar(produtoRequest.descricao()))
                .preco(produtoRequest.preco())
                .quantidade(produtoRequest.quantidade())
                .categoria(limpar(produtoRequest.categoria()))
                .build();
        Produto produtoSalvo = produtoRepository.save(produto);
        return ProdutoResponse.from(produtoSalvo);
    }

    public List<ProdutoResponse> listarProdutos() {
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoResponse::from)
                .toList();
    }


    public ProdutoResponse buscarPorId(Long id){
        Produto produto = buscarOuFalhar(id);
        return ProdutoResponse.from(produto);
    }

    @Transactional
    public ProdutoResponse atualizarProduto(Long id, ProdutoRequest produtoRequest){
        Produto produto = buscarOuFalhar(id);
        produto.setNome(limpar(produtoRequest.nome()));
        produto.setDescricao(limpar(produtoRequest.descricao()));
        produto.setPreco(produtoRequest.preco());
        produto.setQuantidade(produtoRequest.quantidade());
        produto.setCategoria(limpar(produtoRequest.categoria()));
        return ProdutoResponse.from(produtoRepository.save(produto));
    }

    @Transactional
    public ProdutoResponse atualizarParcial(Long id, ProdutoPatchRequest request) {
        Produto produto = buscarOuFalhar(id);

        if (request.nome() != null) produto.setNome(limpar(request.nome()));
        if (request.descricao() != null) produto.setDescricao(limpar(request.descricao()));
        if (request.preco() != null) produto.setPreco(request.preco());
        if (request.quantidade() != null) produto.setQuantidade(request.quantidade());
        if (request.categoria() != null) produto.setCategoria(limpar(request.categoria()));

        return ProdutoResponse.from(produtoRepository.save(produto));
    }


    @Transactional
    public void excluirProduto(Long id){
        Produto produto = buscarOuFalhar(id);
        produtoRepository.delete(produto);
    }


    private String limpar(String texto) {

        return texto == null ? null : texto.trim();
    }

    private Produto buscarOuFalhar(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

}
