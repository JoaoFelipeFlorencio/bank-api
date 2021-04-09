package com.accenture.academico.bankapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.text.ParseException;

import com.accenture.academico.bankapi.model.Agencia;
import com.accenture.academico.bankapi.model.Cliente;
import com.accenture.academico.bankapi.model.ContaCorrente;
import com.accenture.academico.bankapi.service.ContaCorrenteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;


@SpringBootTest
@WebAppConfiguration
public class ContaTest {
    
    @Autowired
    private ContaCorrenteService testesConta;

    
    @Test
    public void buscarContaTest(){
        ContaCorrente buscarConta = testesConta.buscarConta(2L);
        assertEquals("002", buscarConta.getNumero());
        assertEquals("40.00", String.valueOf(buscarConta.getSaldo()));  
        assertEquals("3", String.valueOf(buscarConta.getAgencia().getId()));
        assertEquals("2", String.valueOf(buscarConta.getCliente().getId()));
    }

    @Test
    public void salvarConta() throws ParseException{
        ContaCorrente conta = new ContaCorrente();
        conta.setNumero("10");
        conta.setSaldo(new BigDecimal("245"));
        Agencia agencia1 = new Agencia();
        agencia1.setId(Long.parseLong("4"));
        conta.setAgencia(agencia1);
        Cliente cliente1 = new Cliente();
        cliente1.setId(Long.parseLong("2"));
        conta.setCliente(cliente1);

        testesConta.criarConta(conta);

    

    } 
}
