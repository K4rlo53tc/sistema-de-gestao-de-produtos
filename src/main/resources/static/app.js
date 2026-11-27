// ============================================================
// Gestão de Produtos - frontend
// Fala com a API Spring Boot em /api/produtos (mesma origem).
// ============================================================

const API = "/api/produtos";
const TAMANHO_PAGINA = 5;

// ---------- Elementos da página (seleccionados uma só vez) ----------
const form = document.getElementById("form-produto");
const campoId = document.getElementById("produto-id");
const campoNome = document.getElementById("nome");
const campoDescricao = document.getElementById("descricao");
const campoPreco = document.getElementById("preco");
const campoQuantidade = document.getElementById("quantidade");
const campoCategoria = document.getElementById("categoria");
const tituloForm = document.getElementById("titulo-form");
const btnGuardar = document.getElementById("btn-guardar");
const btnCancelar = document.getElementById("btn-cancelar");
const mensagem = document.getElementById("mensagem");
const tbody = document.getElementById("tabela-produtos");
const vazio = document.getElementById("vazio");
const contador = document.getElementById("contador");
const pesquisa = document.getElementById("pesquisa");
const btnAnterior = document.getElementById("btn-anterior");
const btnSeguinte = document.getElementById("btn-seguinte");
const infoPagina = document.getElementById("info-pagina");

// ---------- Estado ----------
let produtos = [];        // produtos da página actual
let paginaAtual = 0;      // a API conta páginas a partir de 0
let totalProdutos = 0;    // total na base de dados

// ---------- Mensagens ao utilizador ----------
function mostrarMensagem(texto, tipo) {
    mensagem.className = tipo; // "sucesso" ou "erro"
    mensagem.textContent = texto;
}

// Os erros de validação do backend chegam como { campo: "mensagem" }.
function mostrarErros(erros) {
    mensagem.className = "erro";
    mensagem.textContent = "";
    const lista = document.createElement("ul");

    for (const [campo, texto] of Object.entries(erros)) {
        const item = document.createElement("li");
        item.textContent = texto;
        lista.appendChild(item);

        const input = document.getElementById(campo);
        if (input) input.classList.add("invalido");
    }
    mensagem.appendChild(lista);
}

function limparErros() {
    mensagem.className = "";
    mensagem.textContent = "";
    form.querySelectorAll(".invalido").forEach(i => i.classList.remove("invalido"));
}

// Lê a resposta de erro da API e mostra a mensagem certa.
async function tratarErro(resposta) {
    let corpo = null;
    try {
        corpo = await resposta.json();
    } catch (e) {
        // resposta sem JSON
    }

    if (corpo && corpo.erro) {
        mostrarMensagem(corpo.erro, "erro");
    } else if (corpo && resposta.status === 400) {
        mostrarErros(corpo);
    } else {
        mostrarMensagem("Erro inesperado (código " + resposta.status + ").", "erro");
    }
}

// ---------- Formatação ----------
function formatarPreco(valor) {
    return new Intl.NumberFormat("pt-AO", {
        style: "currency",
        currency: "AOA"
    }).format(valor);
}

function formatarData(texto) {
    if (!texto) return "";
    return new Date(texto).toLocaleDateString("pt-PT");
}

// ---------- Tabela ----------
// Usamos textContent (e não innerHTML) para evitar injecção de HTML (XSS).
function criarCelula(texto, classe) {
    const td = document.createElement("td");
    td.textContent = texto;
    if (classe) td.className = classe;
    return td;
}

function desenharTabela() {
    const termo = pesquisa.value.trim().toLowerCase();

    // A pesquisa filtra só os produtos da página actual.
    const visiveis = produtos.filter(p =>
        p.nome.toLowerCase().includes(termo) ||
        p.categoria.toLowerCase().includes(termo)
    );

    tbody.innerHTML = "";

    visiveis.forEach(p => {
        const tr = document.createElement("tr");

        tr.appendChild(criarCelula(p.id));
        tr.appendChild(criarCelula(p.nome));
        tr.appendChild(criarCelula(p.descricao || ""));
        tr.appendChild(criarCelula(formatarPreco(p.preco), "numero"));
        // Stock baixo (menos de 5) fica a vermelho
        tr.appendChild(criarCelula(p.quantidade, p.quantidade < 5 ? "numero stock-baixo" : "numero"));
        tr.appendChild(criarCelula(p.categoria));
        tr.appendChild(criarCelula(formatarData(p.dataCadastro)));

        const acoes = document.createElement("td");
        acoes.className = "acoes";

        const btnEditar = document.createElement("button");
        btnEditar.textContent = "Editar";
        btnEditar.className = "pequeno secundario";
        btnEditar.addEventListener("click", () => iniciarEdicao(p.id));

        const btnExcluir = document.createElement("button");
        btnExcluir.textContent = "Excluir";
        btnExcluir.className = "pequeno perigo";
        btnExcluir.addEventListener("click", () => excluirProduto(p.id, p.nome));

        acoes.appendChild(btnEditar);
        acoes.appendChild(btnExcluir);
        tr.appendChild(acoes);

        tbody.appendChild(tr);
    });

    // Sem pesquisa mostra o total da base de dados; com pesquisa, os resultados.
    contador.textContent = "(" + (termo === "" ? totalProdutos : visiveis.length) + ")";
    vazio.hidden = visiveis.length > 0;
}

// ---------- Operações na API ----------

// LISTAR (GET), uma página de cada vez
async function carregarProdutos() {
    try {
        const resposta = await fetch(API + "?page=" + paginaAtual + "&size=" + TAMANHO_PAGINA);
        if (!resposta.ok) {
            await tratarErro(resposta);
            return;
        }

        const dados = await resposta.json();

        // Se a página pedida já não existe (ex.: apagou o último da página),
        // recua para a última página que existe.
        if (dados.content.length === 0 && paginaAtual > 0) {
            paginaAtual = Math.max(dados.totalPages - 1, 0);
            return carregarProdutos();
        }

        produtos = dados.content;
        totalProdutos = dados.totalElements;
        desenharTabela();

        infoPagina.textContent = "Página " + (dados.number + 1) + " de " + Math.max(dados.totalPages, 1);
        btnAnterior.disabled = dados.first;
        btnSeguinte.disabled = dados.last;
    } catch (e) {
        mostrarMensagem("Não foi possível ligar ao servidor.", "erro");
    }
}

// CADASTRAR (POST) ou ATUALIZAR (PUT), conforme exista id no campo escondido
async function guardarProduto(event) {
    event.preventDefault(); // impede a página de recarregar
    limparErros();

    const id = campoId.value;
    const editar = id !== "";

    // Os campos de input devolvem sempre texto, por isso convertemos os números.
    const produto = {
        nome: campoNome.value,
        descricao: campoDescricao.value,
        preco: campoPreco.value === "" ? null : Number(campoPreco.value),
        quantidade: campoQuantidade.value === "" ? null : Number(campoQuantidade.value),
        categoria: campoCategoria.value
    };

    try {
        const resposta = await fetch(editar ? API + "/" + id : API, {
            method: editar ? "PUT" : "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(produto)
        });

        if (!resposta.ok) {
            await tratarErro(resposta);
            return;
        }

        mostrarMensagem(editar ? "Produto actualizado." : "Produto cadastrado.", "sucesso");
        sairDoModoEdicao();
        await carregarProdutos();
    } catch (e) {
        mostrarMensagem("Não foi possível ligar ao servidor.", "erro");
    }
}

// EXCLUIR (DELETE)
async function excluirProduto(id, nome) {
    if (!confirm('Excluir o produto "' + nome + '"?')) return;

    try {
        const resposta = await fetch(API + "/" + id, { method: "DELETE" });

        if (!resposta.ok) {
            await tratarErro(resposta);
            return;
        }

        mostrarMensagem("Produto excluído.", "sucesso");
        if (campoId.value === String(id)) sairDoModoEdicao();
        await carregarProdutos();
    } catch (e) {
        mostrarMensagem("Não foi possível ligar ao servidor.", "erro");
    }
}

// ---------- Modo de edição ----------
function iniciarEdicao(id) {
    const p = produtos.find(item => item.id === id);
    if (!p) return;

    limparErros();
    campoId.value = p.id;
    campoNome.value = p.nome;
    campoDescricao.value = p.descricao || "";
    campoPreco.value = p.preco;
    campoQuantidade.value = p.quantidade;
    campoCategoria.value = p.categoria;

    tituloForm.textContent = "Editar produto #" + p.id;
    btnGuardar.textContent = "Guardar alterações";
    btnCancelar.hidden = false;

    window.scrollTo({ top: 0, behavior: "smooth" });
    campoNome.focus();
}

function sairDoModoEdicao() {
    form.reset();
    campoId.value = "";
    tituloForm.textContent = "Novo produto";
    btnGuardar.textContent = "Cadastrar";
    btnCancelar.hidden = true;
}

// ---------- Eventos ----------
form.addEventListener("submit", guardarProduto);

btnCancelar.addEventListener("click", () => {
    limparErros();
    sairDoModoEdicao();
});

pesquisa.addEventListener("input", desenharTabela);

btnAnterior.addEventListener("click", () => {
    paginaAtual--;
    carregarProdutos();
});

btnSeguinte.addEventListener("click", () => {
    paginaAtual++;
    carregarProdutos();
});

// ---------- Arranque ----------
carregarProdutos();