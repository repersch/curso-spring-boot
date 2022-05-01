package io.github.repersch.domain.repository;

import io.github.repersch.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByNomeContains(String nome);

    List<Cliente> findByNomeOrIdOrderById(String nome, Integer id);

    boolean existsByNomeContains(String nome);

    @Query(value = "select c from Cliente c where c.nome like :nome")
    List<Cliente> encontrarPorNome( @Param("nome") String nome );

    // retorna todos os pedidos do cliente
    @Query("select c from Cliente c left join fetch c.pedidos where c.id =:id")
    Cliente findClienteFetchPedidos( @Param("id") Integer id );

}
