# Sistema de Gestão de Produtos

API REST para gerir produtos (cadastrar, listar, pesquisar, editar e excluir), com um frontend simples em HTML, CSS e JavaScript servido pelo próprio Spring Boot.

## Tecnologias

- Java 21
- Spring Boot (Web, Data JPA, Validation)
- PostgreSQL
- Gradle
- Lombok
- HTML, CSS e JavaScript (sem frameworks)

## Como correr

1. Instalar Java 21 e PostgreSQL.
2. Criar a base de dados:

   ```sql
   CREATE DATABASE db_sistema_gestao_produtos;
   ```

3. A ligação está em `src/main/resources/application.properties`, com utilizador e password `postgres` por defeito. Para usar outras credenciais, definir as variáveis de ambiente `DB_USER` e `DB_PASSWORD`.
4. Arrancar a aplicação:

   ```bash
   ./gradlew bootRun
   ```

5. Abrir `http://localhost:8080/` no browser.

## Endpoints

| Método | URL | Descrição |
|--------|-----|-----------|
| POST | `/api/produtos` | Cadastrar produto |
| GET | `/api/produtos` | Listar produtos |
| GET | `/api/produtos/{id}` | Buscar por id |
| PUT | `/api/produtos/{id}` | Actualizar todos os campos |
| PATCH | `/api/produtos/{id}` | Actualizar só os campos enviados |
| DELETE | `/api/produtos/{id}` | Excluir produto |

### Exemplo de corpo (POST e PUT)

```json
{
  "nome": "Teclado Mecânico",
  "descricao": "Teclado com switches azuis e luz RGB",
  "preco": 25000.00,
  "quantidade": 15,
  "categoria": "Periféricos"
}
```

### Respostas de erro

- `400 Bad Request`: erros de validação, por campo.

  ```json
  { "nome": "O Nome é Obrigatório.", "preco": "O Preço deve ser superior a zero." }
  ```

- `404 Not Found`: produto inexistente.

  ```json
  { "erro": "Produto com id 999 não encontrado." }
  ```

## Estrutura

```
model       -> entidade Produto
repository  -> acesso à base de dados (Spring Data JPA)
dto         -> ProdutoRequest, ProdutoPatchRequest, ProdutoResponse
service     -> regras de negócio
controller  -> endpoints REST
exception   -> tratamento global de erros
static      -> frontend (index.html, style.css, app.js)
```

## Autor

CJ (Carlos Jr), estudante de Ciências da Computação, Universidade Agostinho Neto.
GitHub: [K4rlo53tc](https://github.com/K4rlo53tc)