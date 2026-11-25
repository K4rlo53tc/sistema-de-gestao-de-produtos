package CJ_DEV.Sistema.de.Gestao.de.Produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O Nome é Obrigatório.")
    @Size(max = 50, message = "O Nome não pode ultrapassar 50 caracteres.")
    @Column(length = 50, nullable = false)
    private String nome;

    @Size(max = 100, message = "A Descrição não pode ultrapassar 100 caracteres.")
    @Column(length = 100)
    private String descricao;

    @NotNull(message = "O Preço é obrigatório.")
    @DecimalMin(value = "0.01", message = "O Preço deve ser superior a zero.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @NotNull(message = "A Quantidade é obrigatória.")
    @Min(value = 0, message = "A Quantidade não pode ser negativa.")
    @Column(nullable = false)
    private Integer quantidade;

    @NotBlank(message = "A Categoria é obrigatória.")
    @Size(max = 50, message = "A Categoria não pode ultrapassar 50 caracteres.")
    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(updatable = false)
    private LocalDateTime dataCadastro;

    @PrePersist
    public void definirDataCadastro() {
        if (this.dataCadastro == null) {
            this.dataCadastro = LocalDateTime.now();
        }
    }

}
