package CJ_DEV.Sistema.de.Gestao.de.Produtos.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProdutoPatchRequest(

        @Pattern(regexp = ".*\\S.*", message = "O Nome não pode estar em branco.")
        @Size(max = 50, message = "O Nome não pode ultrapassar 50 caracteres.")
        String nome,

        @Size(max = 100, message = "A Descrição não pode ultrapassar 100 caracteres.")
        String descricao,

        @DecimalMin(value = "0.01", message = "O Preço deve ser superior a zero.")
        @Digits(integer = 8, fraction = 2, message = "Preço inválido.")
        BigDecimal preco,

        @Min(value = 0, message = "A Quantidade não pode ser negativa.")
        Integer quantidade,

        @Pattern(regexp = ".*\\S.*", message = "A Categoria não pode estar em branco.")
        @Size(max = 50, message = "A Categoria não pode ultrapassar 50 caracteres.")
        String categoria
) {}
