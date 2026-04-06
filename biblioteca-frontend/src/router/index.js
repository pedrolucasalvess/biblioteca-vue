import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Livros from '../views/Livros.vue'
import Cadastro from '../views/Cadastro.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/livros', name: 'Livros', component: Livros },
  { path: '/cadastro', name: 'Cadastro', component: Cadastro },
  { path: '/cadastro/:id', name: 'Editar', component: Cadastro },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router