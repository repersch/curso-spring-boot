package io.github.repersch.domain.repository;

import io.github.repersch.domain.entity.Cliente;
import io.github.repersch.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    // retorna todos os pedidos do cliente
    List<Pedido> findByCliente(Cliente cliente);

    // traz os itens do pedido pelo id do pedido
    @Query("select p from Pedido p left join fetch p.itens where p.id =:id")
    Optional<Pedido> findByIdFetchItens(@Param("id") Integer id);

}
