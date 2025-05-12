package com.messageria.demo.entidade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(name = "Pedido", description = "API de pedidos")
@RepositoryRestResource(collectionResourceRel = "pedidos", path = "pedidos")
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
}
