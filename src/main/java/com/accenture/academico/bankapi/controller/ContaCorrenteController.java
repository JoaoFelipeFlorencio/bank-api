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

import com.accenture.academico.bankapi.model.ContaCorrente;
import com.accenture.academico.bankapi.service.ContaCorrenteService;


@RestController
@RequestMapping("/bank")
public class ContaCorrenteController {
	
	@Autowired
	private ContaCorrenteService service;

	@GetMapping("/contas")
	@ResponseStatus(HttpStatus.OK)
	public List<ContaCorrente> buscaContas() {
		return service.buscarContas();
	}
	
	@GetMapping("/conta/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ContaCorrente buscaConta(@PathVariable("id")Long id) {
		return service.buscarConta(id);
	}
	
	@PostMapping("/conta")
	@ResponseStatus(HttpStatus.CREATED)
	public void salvarConta(@RequestBody ContaCorrente conta) {
		service.criarConta(conta);
	}
	
	@PutMapping("/conta/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void alterarConta(@PathVariable("id") Long id,@RequestBody ContaCorrente conta) {
		this.service.atualizarConta(id, conta);
	}
	
	@DeleteMapping("/conta/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarConta(@PathVariable("id") Long id ) {
		this.service.removerConta(id);
	}
	
}
