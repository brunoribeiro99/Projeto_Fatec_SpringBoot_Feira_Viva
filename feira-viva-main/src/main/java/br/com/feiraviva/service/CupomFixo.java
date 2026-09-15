package br.com.feiraviva.service;

import java.math.BigDecimal;

public class CupomFixo extends Cupom {

    private final BigDecimal valor;

    public CupomFixo(String codigo, String descricao, BigDecimal valor) {
        super(codigo, descricao);
        this.valor = valor;
    }

    @Override
    public BigDecimal calcularDesconto(BigDecimal subtotal) {
        return valor.min(subtotal);      // R7: desconto nunca supera o subtotal
    }
}