package CJ_DEV.Sistema.de.Gestao.de.Produtos.dto;

import CJ_DEV.Sistema.de.Gestao.de.Produtos.model.Produto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProdutoRequest(

        @NotBlank(message = "O Nome é Obrigatório.")
        @Size(max = 50, message = "O Nome não pode ultrapassar 50 caracteres.")
        String nome,

        @Size(max = 100, message = "A Descrição não pode ultrapassar 100 caracteres.")
        String descricao,

        @NotNull(message = "O Preço é obrigatório.")
        @DecimalMin(value = "0.01", message = "O Preço deve ser superior a zero.")
        BigDecimal preco,

        @NotNull(message = "A Quantidade é obrigatória.")
        @Min(value = 0, message = "A Quantidade não pode ser negativa.")
        Integer quantidade,

        @NotBlank(message = "A Categoria é obrigatória.")
        @Size(max = 50, message = "A Categoria não pode ultrapassar 50 caracteres.")
        String categoria
) {

}