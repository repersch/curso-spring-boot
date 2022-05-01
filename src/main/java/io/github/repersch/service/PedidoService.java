package io.github.repersch.service;

import io.github.repersch.domain.entity.Pedido;
import io.github.repersch.domain.enums.StatusPedido;
import io.github.repersch.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar ( PedidoDTO dto );

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, StatusPedido status);

}
