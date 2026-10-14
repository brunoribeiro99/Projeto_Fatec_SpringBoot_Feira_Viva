package br.com.feiraviva.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.feiraviva.config.ConfiguracoesFeiraViva;

@Component("FIXO")
public class FreteFixoStrategy implements StrategyFrete {

    private final ConfiguracoesFeiraViva config;

    public FreteFixoStrategy(ConfiguracoesFeiraViva config) {
        this.config = config;
    }

    @Override
    public BigDecimal calcular(BigDecimal subtotal) {
        return config.getFreteFixo();        // sempre cobra, não importa o subtotal
    }
}