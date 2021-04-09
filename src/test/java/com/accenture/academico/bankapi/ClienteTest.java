package com.accenture.academico.bankapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.text.ParseException;
import com.accenture.academico.bankapi.model.Cliente;
import com.accenture.academico.bankapi.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
public class ClienteTest {

    @Autowired
    private ClienteService testesCliente;

    @Test
    public void buscarClienteTest(){
        Cliente buscarCliente = testesCliente.buscarCliente(1L);
        assertEquals("584.042.520-68", buscarCliente.getCpf());
        assertEquals("Jo", buscarCliente.getNome());
        assertEquals("45a556", buscarCliente.getTelefone());
    }

    @Test
    public void salvarCliente() throws ParseException{
        Cliente cliente = new Cliente();
        cliente.setNome("Cleitin");
        cliente.setCpf("195.027.880-87");
        cliente.setTelefone("4145-2154");
        
 
        testesCliente.salvarCliente(cliente);
     }
 

    
}