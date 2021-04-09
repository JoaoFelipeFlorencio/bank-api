package com.accenture.academico.bankapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.academico.bankapi.model.Extrato;
import com.accenture.academico.bankapi.service.ExtratoService;

@RestController
@RequestMapping("/bank")
public class ExtratoController {

	@Autowired
	private ExtratoService service;

	@GetMapping("/extrato/{id}")
	@ResponseStatus(HttpStatus.OK)
	public List<Extrato> buscarExtrato(@PathVariable("id") Long id) {
		return service.buscarExtrato(id);
	}

}
