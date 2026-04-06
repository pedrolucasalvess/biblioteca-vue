<template>
  <div class="livros-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">Acervo</h1>
        <p class="page-sub">{{ livros.length }} livro(s) encontrado(s)</p>
      </div>
      <router-link to="/cadastro" class="btn-add">+ Novo Livro</router-link>
    </div>

    <div class="search-bar">
      <span class="search-icon">🔍</span>
      <input
        v-model="busca"
        @input="onBusca"
        type="text"
        placeholder="Buscar por título..."
        class="search-input"
      />
    </div>

    <div v-if="carregando" class="estado-vazio">
      <span>Carregando...</span>
    </div>

    <div v-else-if="livros.length === 0" class="estado-vazio">
      <span class="vazio-icon">📭</span>
      <p>Nenhum livro encontrado.</p>
      <router-link to="/cadastro" class="link-cadastrar">Cadastrar o primeiro livro →</router-link>
    </div>

    <div v-else class="livros-grid">
      <div class="livro-card" v-for="livro in livros" :key="livro.id">
        <div class="livro-genero">{{ livro.genero }}</div>
        <h2 class="livro-titulo">{{ livro.titulo }}</h2>
        <p class="livro-autor">{{ livro.autor }} · {{ livro.anoPublicacao }}</p>
        <p class="livro-desc" v-if="livro.descricao">{{ livro.descricao }}</p>
        <div class="livro-actions">
          <router-link :to="`/cadastro/${livro.id}`" class="btn-edit">✏️ Editar</router-link>
          <button class="btn-delete" @click="confirmarExclusao(livro)">🗑️ Excluir</button>
        </div>
      </div>
    </div>

    <!-- Modal de confirmação -->
    <div v-if="livroParaExcluir" class="modal-overlay" @click.self="livroParaExcluir = null">
      <div class="modal">
        <h3>Excluir livro?</h3>
        <p>Tem certeza que deseja excluir <strong>{{ livroParaExcluir.titulo }}</strong>? Esta ação não pode ser desfeita.</p>
        <div class="modal-actions">
          <button class="btn-cancel" @click="livroParaExcluir = null">Cancelar</button>
          <button class="btn-confirm-delete" @click="excluir">Excluir</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { livroService } from '../services/api'

const livros = ref([])
const busca = ref('')
const carregando = ref(true)
const livroParaExcluir = ref(null)
let debounceTimer = null

async function carregarLivros() {
  carregando.value = true
  try {
    const res = await livroService.listarTodos()
    livros.value = res.data
  } catch (e) {
    alert('Erro ao carregar livros. Verifique se o backend está rodando.')
  } finally {
    carregando.value = false
  }
}

function onBusca() {
  clearTimeout(debounceTimer)
  debounceTimer = setTimeout(async () => {
    if (busca.value.trim() === '') {
      carregarLivros()
    } else {
      const res = await livroService.buscarPorTitulo(busca.value)
      livros.value = res.data
    }
  }, 400)
}

function confirmarExclusao(livro) {
  livroParaExcluir.value = livro
}

async function excluir() {
  try {
    await livroService.deletar(livroParaExcluir.value.id)
    livroParaExcluir.value = null
    carregarLivros()
  } catch (e) {
    alert('Erro ao excluir livro.')
  }
}

onMounted(carregarLivros)
</script>

<style scoped>
.livros-page { display: flex; flex-direction: column; gap: 1.5rem; }

.page-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 1rem;
  flex-wrap: wrap;
}

.page-title {
  font-family: var(--font-display);
  font-size: 2rem;
  color: var(--text);
}

.page-sub { color: var(--text-muted); font-size: 0.85rem; margin-top: 0.2rem; }

.btn-add {
  background: var(--accent);
  color: var(--bg);
  padding: 0.6rem 1.4rem;
  border-radius: var(--radius);
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.2s;
  white-space: nowrap;
}
.btn-add:hover { background: var(--accent2); transform: translateY(-1px); }

/* Search */
.search-bar {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 0 1rem;
}

.search-icon { font-size: 0.95rem; }

.search-input {
  background: transparent;
  border: none;
  padding: 0.85rem 0;
  flex: 1;
  font-size: 0.95rem;
}

/* Estados */
.estado-vazio {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.75rem;
  padding: 4rem 0;
  color: var(--text-muted);
  text-align: center;
}

.vazio-icon { font-size: 2.5rem; }
.link-cadastrar { color: var(--accent); font-size: 0.9rem; }
.link-cadastrar:hover { text-decoration: underline; }

/* Grid */
.livros-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1rem;
}

.livro-card {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
  transition: border-color 0.2s, transform 0.2s;
}

.livro-card:hover {
  border-color: var(--accent);
  transform: translateY(-2px);
}

.livro-genero {
  font-size: 0.7rem;
  letter-spacing: 0.15em;
  text-transform: uppercase;
  color: var(--accent);
  font-weight: 500;
}

.livro-titulo {
  font-family: var(--font-display);
  font-size: 1.2rem;
  color: var(--text);
  line-height: 1.3;
}

.livro-autor { font-size: 0.85rem; color: var(--text-muted); }

.livro-desc {
  font-size: 0.85rem;
  color: var(--text-muted);
  line-height: 1.6;
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.livro-actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.75rem;
  padding-top: 0.75rem;
  border-top: 1px solid var(--border);
}

.btn-edit, .btn-delete {
  flex: 1;
  padding: 0.5rem;
  border-radius: calc(var(--radius) - 2px);
  font-size: 0.8rem;
  font-weight: 500;
  text-align: center;
  transition: all 0.2s;
}

.btn-edit {
  background: var(--bg2);
  border: 1px solid var(--border);
  color: var(--text);
}
.btn-edit:hover { border-color: var(--accent); color: var(--accent); }

.btn-delete {
  background: transparent;
  border: 1px solid transparent;
  color: var(--text-muted);
}
.btn-delete:hover { border-color: var(--danger); color: var(--danger); }

/* Modal */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 200;
  backdrop-filter: blur(4px);
}

.modal {
  background: var(--bg2);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 2rem;
  max-width: 400px;
  width: 90%;
}

.modal h3 {
  font-family: var(--font-display);
  font-size: 1.3rem;
  margin-bottom: 0.75rem;
}

.modal p {
  color: var(--text-muted);
  font-size: 0.9rem;
  line-height: 1.6;
  margin-bottom: 1.5rem;
}

.modal strong { color: var(--text); }

.modal-actions { display: flex; gap: 0.75rem; }

.btn-cancel {
  flex: 1;
  padding: 0.65rem;
  background: var(--surface);
  border: 1px solid var(--border);
  color: var(--text);
  border-radius: var(--radius);
  font-size: 0.9rem;
}
.btn-cancel:hover { border-color: var(--accent); }

.btn-confirm-delete {
  flex: 1;
  padding: 0.65rem;
  background: var(--danger);
  color: white;
  border-radius: var(--radius);
  font-size: 0.9rem;
  font-weight: 500;
}
.btn-confirm-delete:hover { opacity: 0.85; }
</style>