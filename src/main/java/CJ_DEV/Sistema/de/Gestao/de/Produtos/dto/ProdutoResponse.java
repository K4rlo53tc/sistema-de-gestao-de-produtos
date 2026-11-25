package CJ_DEV.Sistema.de.Gestao.de.Produtos.dto;

import CJ_DEV.Sistema.de.Gestao.de.Produtos.model.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer quantidade,
        String categoria,
        LocalDateTime dataCadastro
) {

    public static ProdutoResponse from(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getCategoria(),
                produto.getDataCadastro()
        );
    }
}
