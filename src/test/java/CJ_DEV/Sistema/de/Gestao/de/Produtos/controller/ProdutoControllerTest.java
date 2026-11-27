package CJ_DEV.Sistema.de.Gestao.de.Produtos.controller;

import CJ_DEV.Sistema.de.Gestao.de.Produtos.dto.ProdutoResponse;
import CJ_DEV.Sistema.de.Gestao.de.Produtos.exception.ProdutoNaoEncontradoException;
import CJ_DEV.Sistema.de.Gestao.de.Produtos.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @Test
    void buscarPorId_quandoNaoExiste_devolve404() throws Exception {
        when(produtoService.buscarPorId(99L)).thenThrow(new ProdutoNaoEncontradoException(99L));

        mockMvc.perform(get("/api/produtos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Produto com id 99 não encontrado."));
    }

    @Test
    void cadastrar_comNomeVazio_devolve400() throws Exception {
        String json = """
                {"nome": "", "preco": 0, "quantidade": 5, "categoria": "Periféricos"}
                """;

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").value("O Nome é Obrigatório."));
    }

    @Test
    void cadastrar_comDadosValidos_devolve201() throws Exception {

        ProdutoResponse resposta = new ProdutoResponse(
                1L, "Teclado", null, new BigDecimal("25000.00"), 10, "Periféricos", LocalDateTime.now()
        );
        when(produtoService.cadastrarProduto(any())).thenReturn(resposta);
        String json = """
                {"nome": "Teclado", "preco": 25000.00, "quantidade": 10, "categoria": "Periféricos"}
                """;
        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

    }

}