package com.messageria.demo.entidade;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String itens;
    private long quantidade;
    private long valor;
    @jakarta.persistence.Column(updatable = false)
    @jakarta.persistence.Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime createdAt;
    @jakarta.persistence.Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
    @org.hibernate.annotations.UpdateTimestamp
    @jakarta.persistence.Column(insertable = false)
    private LocalDateTime updatedAt;
}
