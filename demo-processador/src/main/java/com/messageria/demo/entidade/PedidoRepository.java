package com.messageria.demo.entidade;

import org.springframework.data.jpa.repository.JpaRepository;



public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
}
