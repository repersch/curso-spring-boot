package io.github.repersch.rest.controller;

import io.github.repersch.domain.entity.Produto;
import io.github.repersch.domain.repository.ProdutoRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }


    @GetMapping("/{id}")
    public Produto getProdutoById(@PathVariable Integer id) {
        return produtoRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }


    @PostMapping
    @ResponseStatus(CREATED)
    public Produto save(@RequestBody @Valid Produto produto) {
        return produtoRepository.save(produto);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        produtoRepository
                .findById(id)
                .map(produto -> {
                    produtoRepository.deleteById(produto.getId());
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }


    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public Produto update(@PathVariable Integer id, @RequestBody @Valid Produto produto) {
        return produtoRepository
                .findById(id)
                .map(produtoExitente -> {
                    produto.setId(produtoExitente.getId());
                    produtoRepository.save(produto);
                    return produto;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }


    @GetMapping
    public List<Produto> find(Produto filtro ) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        return produtoRepository.findAll(Example.of(filtro, matcher));
    }

}
