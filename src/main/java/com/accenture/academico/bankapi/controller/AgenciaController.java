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

import com.accenture.academico.bankapi.model.Agencia;
import com.accenture.academico.bankapi.service.AgenciaService;







@RestController
@RequestMapping("/bank")
public class AgenciaController {

	@Autowired
	private AgenciaService service;
	
	@GetMapping("/agencias")
	@ResponseStatus(HttpStatus.OK)
	public List<Agencia> buscaAgencias() {
		return service.buscarAgencias();
	}
	
	@GetMapping("/agencia/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Agencia buscaAgencia(@PathVariable("id")Long id) {
		return service.buscarAgencia(id);
	}
	
	@PostMapping("/agencia")
	@ResponseStatus(HttpStatus.CREATED)
	public void salvarAgencia(@RequestBody Agencia agencia) {
		service.salvarAgencia(agencia);
	}
	
	@PutMapping("/agencia/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void alterarAgencia(@PathVariable("id") Long id,@RequestBody Agencia agencia) {
		this.service.atualizarAgencia(id, agencia);
	}
	
	@DeleteMapping("/agencia/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarAgencia(@PathVariable("id") Long id ) {
		this.service.removerAgencia(id);
	}
}
