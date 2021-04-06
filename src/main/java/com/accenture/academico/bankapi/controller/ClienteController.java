package com.accenture.academico.bankapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.bankapi.model.Cliente;
import com.accenture.academico.bankapi.service.ClienteService;







@RestController
@RequestMapping("/bank")
public class ClienteController {

	@Autowired
	private ClienteService service;
	
	@GetMapping("/clientes")
	@ResponseStatus(HttpStatus.OK)
	public List<Cliente> buscaClientes() {
		return service.buscarClientes();
	}
	
	@GetMapping("/cliente/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Cliente buscaCliente(@PathVariable("id")Long id) {
		return service.buscarCliente(id);
	}
	
	@PostMapping("/cliente")
	@ResponseStatus(HttpStatus.CREATED)
	public void salvarCliente(@RequestBody Cliente cliente) {
		service.salvarCliente(cliente);
	}
	
	@PutMapping("/cliente/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void alterarCliente(@PathVariable("id") long id,@RequestBody Cliente cliente) {
		this.service.atualizarCliente(id, cliente);
	}
	
	@DeleteMapping("/cliente/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarCliente(@PathVariable("id") long id ) {
		this.service.removerCliente(id);
	}
}
