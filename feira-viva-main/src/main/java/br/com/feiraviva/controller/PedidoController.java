package br.com.feiraviva.controller;

import br.com.feiraviva.dto.*;
import br.com.feiraviva.security.ClienteAutenticado;
import br.com.feiraviva.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Tag(name="Pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Finalizar pedido",
            description = "Converte o carrinho em pedido: snapshot de preço, baixa de estoque (R1), "
                    + "frete pela estratégia vigente. Limpa itens, cupom e estratégia do carrinho.")
    @ApiResponse(responseCode = "201", description = "Pedido criado (status CRIADO)")
    @ApiResponse(responseCode = "400", description = "DTO inválido")
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    @ApiResponse(responseCode = "409", description = "Carrinho vazio ou estoque insuficiente")
    public PedidoResponseDTO finalizar(
            @AuthenticationPrincipal ClienteAutenticado cliente,
            @Valid @RequestBody PedidoRequestDTO dto) {
        return pedidoService.finalizar(cliente.id(), dto);
    }

    @GetMapping
    @Operation(summary = "Histórico de pedidos",
            description = "Lista os pedidos do cliente em ordem decrescente de data.")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos")
    public List<PedidoResponseDTO> historico(
            @AuthenticationPrincipal ClienteAutenticado cliente) {
        return pedidoService.historico(cliente.id());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar pedido",
            description = "Retorna o pedido com itens (snapshot de preço).")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    public PedidoResponseDTO buscar(
            @AuthenticationPrincipal ClienteAutenticado cliente,
            @Parameter(description = "ID do pedido", required = true, example = "1")
            @PathVariable Long id) {
        return pedidoService.buscar(cliente.id(), id);
    }

    @PostMapping("/{id}/cancelamento")
    @Operation(summary = "Cancelar pedido",
            description = "Regra R4: só cancela pedidos com status CRIADO e devolve o estoque.")
    @ApiResponse(responseCode = "200", description = "Pedido cancelado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @ApiResponse(responseCode = "409", description = "Pedido não está com status CRIADO")
    public PedidoResponseDTO cancelar(
            @AuthenticationPrincipal ClienteAutenticado cliente,
            @Parameter(description = "ID do pedido", required = true, example = "1")
            @PathVariable Long id) {
        return pedidoService.cancelar(cliente.id(), id);
    }
}