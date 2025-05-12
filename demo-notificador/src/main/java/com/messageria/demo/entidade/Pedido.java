package com.messageria.demo.entidade;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long id;
    private String nome;
    private String itens;
    private long quantidade;
    private long valor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
