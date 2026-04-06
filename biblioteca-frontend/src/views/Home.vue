<template>
  <div class="home">
    <div class="hero">
      <p class="hero-sub">Sistema de Gerenciamento</p>
      <h1 class="hero-title">Sua Biblioteca<br /><em>Digital</em></h1>
      <p class="hero-desc">
        Organize, cadastre e gerencie seu acervo de livros de forma simples e elegante.
      </p>
      <div class="hero-actions">
        <router-link to="/livros" class="btn btn-primary">Ver Acervo</router-link>
        <router-link to="/cadastro" class="btn btn-outline">Cadastrar Livro</router-link>
      </div>
    </div>

    <div class="stats-row">
      <div class="stat-card">
        <span class="stat-num">{{ totalLivros }}</span>
        <span class="stat-label">Livros cadastrados</span>
      </div>
      <div class="stat-card">
        <span class="stat-num">📖</span>
        <span class="stat-label">CRUD completo</span>
      </div>
      <div class="stat-card">
        <span class="stat-num">⚡</span>
        <span class="stat-label">API REST + PostgreSQL</span>
      </div>
    </div>

    <div class="features">
      <div class="feature-card" v-for="f in features" :key="f.title">
        <span class="feature-icon">{{ f.icon }}</span>
        <h3>{{ f.title }}</h3>
        <p>{{ f.desc }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { livroService } from '../services/api'

const totalLivros = ref(0)

const features = [
  { icon: '📝', title: 'Cadastrar', desc: 'Adicione novos livros com título, autor, ano, gênero e descrição.' },
  { icon: '🔍', title: 'Buscar', desc: 'Encontre livros pelo título rapidamente com busca em tempo real.' },
  { icon: '✏️', title: 'Editar', desc: 'Atualize as informações de qualquer livro do acervo.' },
  { icon: '🗑️', title: 'Excluir', desc: 'Remova livros que não fazem mais parte do seu acervo.' },
]

onMounted(async () => {
  try {
    const res = await livroService.stats()
    totalLivros.value = res.data.total
  } catch (e) {
    totalLivros.value = '—'
  }
})
</script>

<style scoped>
.home { display: flex; flex-direction: column; gap: 3rem; }

.hero { text-align: center; padding: 3rem 0 1rem; }

.hero-sub {
  font-size: 0.8rem;
  letter-spacing: 0.2em;
  text-transform: uppercase;
  color: var(--accent);
  margin-bottom: 1rem;
}

.hero-title {
  font-family: var(--font-display);
  font-size: clamp(2.5rem, 6vw, 4.5rem);
  line-height: 1.1;
  margin-bottom: 1.2rem;
  color: var(--text);
}

.hero-title em {
  font-style: italic;
  color: var(--accent);
}

.hero-desc {
  color: var(--text-muted);
  font-size: 1.05rem;
  max-width: 500px;
  margin: 0 auto 2rem;
  line-height: 1.7;
}

.hero-actions { display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap; }

.btn {
  padding: 0.75rem 1.8rem;
  border-radius: var(--radius);
  font-size: 0.95rem;
  font-weight: 500;
  letter-spacing: 0.03em;
  transition: all 0.2s;
  display: inline-block;
}

.btn-primary { background: var(--accent); color: var(--bg); }
.btn-primary:hover { background: var(--accent2); transform: translateY(-1px); }

.btn-outline {
  border: 1px solid var(--border);
  color: var(--text);
  background: transparent;
}
.btn-outline:hover { border-color: var(--accent); color: var(--accent); }

/* Stats */
.stats-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 1rem;
}

.stat-card {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.4rem;
  text-align: center;
}

.stat-num {
  font-family: var(--font-display);
  font-size: 2rem;
  color: var(--accent);
}

.stat-label {
  font-size: 0.8rem;
  color: var(--text-muted);
  letter-spacing: 0.05em;
  text-transform: uppercase;
}

/* Features */
.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 1rem;
}

.feature-card {
  background: var(--bg2);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 1.5rem;
  transition: border-color 0.2s;
}

.feature-card:hover { border-color: var(--accent); }

.feature-icon { font-size: 1.5rem; display: block; margin-bottom: 0.75rem; }

.feature-card h3 {
  font-family: var(--font-display);
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
  color: var(--text);
}

.feature-card p { font-size: 0.9rem; color: var(--text-muted); line-height: 1.6; }
</style>