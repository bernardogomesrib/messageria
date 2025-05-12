package com.messageria.demo.entidade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;



public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
}
