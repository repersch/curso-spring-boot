package io.github.repersch.service.impl;

import io.github.repersch.domain.entity.Cliente;
import io.github.repersch.domain.entity.ItemPedido;
import io.github.repersch.domain.entity.Pedido;
import io.github.repersch.domain.entity.Produto;
import io.github.repersch.domain.enums.StatusPedido;
import io.github.repersch.domain.repository.ClienteRepository;
import io.github.repersch.domain.repository.ItemPedidoRepository;
import io.github.repersch.domain.repository.PedidoRepository;
import io.github.repersch.domain.repository.ProdutoRepository;
import io.github.repersch.exception.PedidoNaoEncontradoException;
import io.github.repersch.exception.RegraNegocioException;
import io.github.repersch.rest.dto.ItemPedidoDTO;
import io.github.repersch.rest.dto.PedidoDTO;
import io.github.repersch.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // gera um construtor com todos os argumentos obrigatórios(final -> pq precisam ser instânciados quando instância a classe)
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;


    @Override
    @Transactional // se der algum erro no método dá rollback
    public Pedido salvar(PedidoDTO dto) {

        // primeiro: busca o cliente
        Integer idCliente = dto.getCliente();
        Cliente cliente = clienteRepository
                                .findById(idCliente)
                                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido."));

        // segundo: cria o pedido
        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemPedidos = converterItens(pedido, dto.getItens());
        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itemPedidos);
        pedido.setItens(itemPedidos);

        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidoRepository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido status) {
        pedidoRepository
                .findById(id)
                .map(pedido -> {
                    pedido.setStatus(status);
                    return pedidoRepository.save(pedido);
                }).orElseThrow(PedidoNaoEncontradoException::new);
    }


    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens) {
        if (itens.isEmpty()) {
            throw new RegraNegocioException("Não é possível realizar um pedido sem itens.");
        }

        return itens.stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtoRepository
                                            .findById(idProduto)
                                            .orElseThrow(() -> new RegraNegocioException(
                                                    "Código de produto inválido: " + idProduto
                                            ));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());


    }
}
