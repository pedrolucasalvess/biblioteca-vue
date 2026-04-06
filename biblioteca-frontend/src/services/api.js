import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
})

export const livroService = {
  listarTodos: () => api.get('/livros'),
  buscarPorId: (id) => api.get(`/livros/${id}`),
  buscarPorTitulo: (titulo) => api.get(`/livros/buscar?titulo=${titulo}`),
  stats: () => api.get('/livros/stats'),
  criar: (livro) => api.post('/livros', livro),
  atualizar: (id, livro) => api.put(`/livros/${id}`, livro),
  deletar: (id) => api.delete(`/livros/${id}`),
}