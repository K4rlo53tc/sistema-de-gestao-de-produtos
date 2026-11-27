package CJ_DEV.Sistema.de.Gestao.de.Produtos.controller;

import CJ_DEV.Sistema.de.Gestao.de.Produtos.dto.ProdutoPatchRequest;
import CJ_DEV.Sistema.de.Gestao.de.Produtos.dto.ProdutoRequest;
import CJ_DEV.Sistema.de.Gestao.de.Produtos.dto.ProdutoResponse;
import CJ_DEV.Sistema.de.Gestao.de.Produtos.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrar(@Valid @RequestBody ProdutoRequest request) {
        ProdutoResponse response = produtoService.cadastrarProduto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<Page<ProdutoResponse>> listar(@PageableDefault (size = 10, sort = "id")Pageable pageable) {
        return ResponseEntity.ok(produtoService.listarProdutos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id,
                                                     @Valid @RequestBody ProdutoRequest request) {
        return ResponseEntity.ok(produtoService.atualizarProduto(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizarParcial(@PathVariable Long id,
                                                            @Valid @RequestBody ProdutoPatchRequest request) {
        return ResponseEntity.ok(produtoService.atualizarParcial(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluirProduto(id);
        return ResponseEntity.noContent().build();
    }

}
