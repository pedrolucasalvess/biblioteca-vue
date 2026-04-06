package com.biblioteca.api.service;

import com.biblioteca.api.model.Livro;
import com.biblioteca.api.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository repository;

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    public List<Livro> listar() {
        return repository.findAll();
    }

    public Livro buscar(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Livro criar(Livro livro) {
        return repository.save(livro);
    }

    public Livro atualizar(Long id, Livro livro) {
        livro.setId(id);
        return repository.save(livro);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}