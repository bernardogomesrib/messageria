package com.messageria.demo.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.messageria.demo.entidade.Pedido;
import com.messageria.demo.entidade.PedidoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoRepository pedidoRepository;

    @GetMapping
    public Page<Pedido> listarPedidos(@RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size) {
        Pageable pageable =  PageRequest.of(page, size, Sort.by("id").descending());
        return pedidoRepository.findAll(pageable);
    }



    @GetMapping("{id}")
    public Pedido buscarPedidoPorId(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com o ID: " + id));
    }
}
