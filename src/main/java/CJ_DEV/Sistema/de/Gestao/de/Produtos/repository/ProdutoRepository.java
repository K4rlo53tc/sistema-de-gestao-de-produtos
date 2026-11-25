package CJ_DEV.Sistema.de.Gestao.de.Produtos.repository;

import CJ_DEV.Sistema.de.Gestao.de.Produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
