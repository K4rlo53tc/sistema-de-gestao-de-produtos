package CJ_DEV.Sistema.de.Gestao.de.Produtos.exception;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException(Long id) {
        super("Produto com id " + id + " não encontrado.");
    }
}
