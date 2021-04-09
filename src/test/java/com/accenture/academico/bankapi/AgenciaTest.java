package com.accenture.academico.bankapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;

import com.accenture.academico.bankapi.model.Agencia;
import com.accenture.academico.bankapi.service.AgenciaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
public class AgenciaTest {

    @Autowired
    private AgenciaService testesAgencia;


    @Test
    public void buscarAgenciaTest(){
        Agencia buscarAgencia = testesAgencia.buscarAgencia(3L);
        assertEquals("Rua dos Padres", buscarAgencia.getEndereco());
        assertEquals("2", buscarAgencia.getNumero());
        assertEquals("1478-8520", buscarAgencia.getTelefone());

    }

    @Test
    public void salvarAgencia() throws ParseException{
       Agencia agencia = new Agencia();
       agencia.setNumero("4014-5248");
       agencia.setEndereco("endereco");
       agencia.setTelefone("telefone");

       testesAgencia.salvarAgencia(agencia);
    }



    
}