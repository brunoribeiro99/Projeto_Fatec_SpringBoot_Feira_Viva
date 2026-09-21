package br.com.feiraviva.controller;

import br.com.feiraviva.dto.*;
import br.com.feiraviva.service.CarrinhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
@Tag(name = "Carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @GetMapping
    @Operation(summary = "Obter carrinho do cliente",
            description = "Retorna itens, cupom, estratégia de frete e totais calculados.")
    @ApiResponse(responseCode = "200", description = "Carrinho retornado (pode estar vazio)")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public CarrinhoResponseDTO obter(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @RequestParam Long clienteId) {
        return carrinhoService.obter(clienteId);
    }

    @PostMapping("/itens")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adicionar item ao carrinho",
            description = "Aplica a regra R1 (estoque); soma quantidade se o produto já estiver no carrinho.")
    @ApiResponse(responseCode = "201", description = "Item adicionado")
    @ApiResponse(responseCode = "400", description = "DTO inválido (quantidade < 1)")
    @ApiResponse(responseCode = "404", description = "Cliente ou produto não encontrado")
    @ApiResponse(responseCode = "409", description = "Produto sem estoque")
    public CarrinhoResponseDTO adicionar(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @RequestParam Long clienteId,
            @Valid @RequestBody ItemCarrinhoDTO dto) {
        return carrinhoService.adicionarItem(clienteId, dto);
    }

    // ... PUT /itens/{itemId}, DELETE /itens/{itemId}, POST /cupom, DELETE /cupom, POST /frete
    //     anotados no mesmo padrão

    @PutMapping("/itens/{itemId}")
    public CarrinhoResponseDTO alterar(@RequestParam Long clienteId,
                                       @PathVariable Long itemId,
                                       @RequestParam int quantidade) {
        return carrinhoService.alterarQuantidade(clienteId, itemId, quantidade);
    }

    @DeleteMapping("/itens/{itemId}")
    public CarrinhoResponseDTO remover(@RequestParam Long clienteId,
                                       @PathVariable Long itemId) {
        return carrinhoService.removerItem(clienteId, itemId);
    }
    @PostMapping("/cupom")
    public CarrinhoResponseDTO aplicarCupom(@RequestParam Long clienteId,
                                            @RequestParam String codigo) {
        return carrinhoService.aplicarCupom(clienteId, codigo);
    }

    @DeleteMapping("/cupom")
    public CarrinhoResponseDTO removerCupom(@RequestParam Long clienteId) {
        return carrinhoService.removerCupom(clienteId);
    }

    @PostMapping("/frete")
    public CarrinhoResponseDTO definirFrete(@RequestParam Long clienteId,
                                            @RequestParam String tipo) {
        return carrinhoService.definirEstrategiaFrete(clienteId, tipo);
    }
}