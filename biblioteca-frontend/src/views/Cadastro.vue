<template>
  <div class="cadastro-page">
    <div class="page-header">
      <router-link to="/livros" class="btn-voltar">← Voltar</router-link>
      <h1 class="page-title">{{ isEdicao ? 'Editar Livro' : 'Novo Livro' }}</h1>
    </div>

    <form class="form-card" @submit.prevent="salvar">
      <div class="form-grid">
        <div class="form-group">
          <label>Título *</label>
          <input v-model="form.titulo" type="text" placeholder="Ex: Dom Casmurro" required />
        </div>

        <div class="form-group">
          <label>Autor *</label>
          <input v-model="form.autor" type="text" placeholder="Ex: Machado de Assis" required />
        </div>

        <div class="form-group">
          <label>Ano de Publicação *</label>
          <input v-model.number="form.anoPublicacao" type="number" placeholder="Ex: 1899" min="1" max="2099" required />
        </div>

        <div class="form-group">
          <label>Gênero *</label>
          <select v-model="form.genero" required>
            <option value="" disabled>Selecione um gênero</option>
            <option>Romance</option>
            <option>Ficção Científica</option>
            <option>Fantasia</option>
            <option>Terror</option>
            <option>Thriller</option>
            <option>Biografia</option>
            <option>História</option>
            <option>Autoajuda</option>
            <option>Técnico</option>
            <option>Poesia</option>
            <option>Outro</option>
          </select>
        </div>

        <div class="form-group">
          <label>ISBN</label>
          <input v-model="form.isbn" type="text" placeholder="Ex: 978-3-16-148410-0" />
        </div>

        <div class="form-group full-width">
          <label>Descrição</label>
          <textarea v-model="form.descricao" rows="4" placeholder="Uma breve sinopse do livro..."></textarea>
        </div>
      </div>

      <div class="form-footer">
        <router-link to="/livros" class="btn-cancel">Cancelar</router-link>
        <button type="submit" class="btn-salvar" :disabled="salvando">
          {{ salvando ? 'Salvando...' : (isEdicao ? '✓ Atualizar' : '+ Cadastrar') }}
        </button>
      </div>

      <p v-if="erro" class="mensagem-erro">⚠ {{ erro }}</p>
      <p v-if="sucesso" class="mensagem-sucesso">✓ {{ sucesso }}</p>
    </form>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { livroService } from '../services/api'

const route = useRoute()
const router = useRouter()

const isEdicao = computed(() => !!route.params.id)
const salvando = ref(false)
const erro = ref('')
const sucesso = ref('')

const form = reactive({
  titulo: '',
  autor: '',
  anoPublicacao: '',
  genero: '',
  isbn: '',
  descricao: '',
})

async function carregarLivro() {
  if (!isEdicao.value) return
  try {
    const res = await livroService.buscarPorId(route.params.id)
    Object.assign(form, res.data)
  } catch (e) {
    erro.value = 'Livro não encontrado.'
  }
}

async function salvar() {
  salvando.value = true
  erro.value = ''
  sucesso.value = ''
  try {
    if (isEdicao.value) {
      await livroService.atualizar(route.params.id, form)
      sucesso.value = 'Livro atualizado com sucesso!'
    } else {
      await livroService.criar(form)
      sucesso.value = 'Livro cadastrado com sucesso!'
      Object.assign(form, { titulo: '', autor: '', anoPublicacao: '', genero: '', isbn: '', descricao: '' })
    }
    setTimeout(() => router.push('/livros'), 1200)
  } catch (e) {
    erro.value = 'Erro ao salvar. Verifique os campos e tente novamente.'
  } finally {
    salvando.value = false
  }
}

onMounted(carregarLivro)
</script>

<style scoped>
.cadastro-page { display: flex; flex-direction: column; gap: 1.5rem; max-width: 700px; }

.page-header { display: flex; align-items: center; gap: 1rem; }

.btn-voltar {
  font-size: 0.85rem;
  color: var(--text-muted);
  transition: color 0.2s;
  padding: 0.3rem 0;
}
.btn-voltar:hover { color: var(--accent); }

.page-title {
  font-family: var(--font-display);
  font-size: 1.8rem;
}

/* Form */
.form-card {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.2rem;
}

.form-group { display: flex; flex-direction: column; gap: 0.4rem; }

.full-width { grid-column: 1 / -1; }

label {
  font-size: 0.8rem;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--text-muted);
  font-weight: 500;
}

textarea { resize: vertical; }

select option { background: var(--bg2); }

/* Footer */
.form-footer {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  padding-top: 0.5rem;
  border-top: 1px solid var(--border);
}

.btn-cancel {
  padding: 0.65rem 1.4rem;
  background: transparent;
  border: 1px solid var(--border);
  color: var(--text-muted);
  border-radius: var(--radius);
  font-size: 0.9rem;
  transition: all 0.2s;
}
.btn-cancel:hover { border-color: var(--text-muted); color: var(--text); }

.btn-salvar {
  padding: 0.65rem 1.8rem;
  background: var(--accent);
  color: var(--bg);
  border-radius: var(--radius);
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.2s;
}
.btn-salvar:hover:not(:disabled) { background: var(--accent2); }
.btn-salvar:disabled { opacity: 0.6; cursor: not-allowed; }

.mensagem-erro { color: var(--danger); font-size: 0.9rem; text-align: center; }
.mensagem-sucesso { color: var(--success); font-size: 0.9rem; text-align: center; }

@media (max-width: 500px) {
  .form-grid { grid-template-columns: 1fr; }
}
</style>