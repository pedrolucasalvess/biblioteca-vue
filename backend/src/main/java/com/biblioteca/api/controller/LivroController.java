package com.biblioteca.api.controller;

import com.biblioteca.api.model.Livro;
import com.biblioteca.api.service.LivroService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/livros")
@CrossOrigin
public class LivroController {

    private final LivroService service;

    public LivroController(LivroService service) {
        this.service = service;
    }

    @GetMapping
    public List<Livro> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Livro buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping("/buscar")
    public List<Livro> buscarPorTitulo(@RequestParam String titulo) {
        return service.buscarPorTitulo(titulo);
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return Map.of("total", service.totalLivros());
    }

    @PostMapping
    public Livro criar(@RequestBody Livro livro) {
        return service.criar(livro);
    }

    @PutMapping("/{id}")
    public Livro atualizar(@PathVariable Long id, @RequestBody Livro livro) {
        return service.atualizar(id, livro);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}