package com.accenture.academico.bankapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.academico.bankapi.model.Extrato;
import com.accenture.academico.bankapi.repository.ExtratoRepository;

@Service
public class ExtratoService {

	@Autowired
	private ExtratoRepository repository;

	public void inserirExtrato(Extrato extrato) {
		this.repository.save(extrato);
	}

	public List<Extrato> buscarExtrato(Long id) {
		return this.repository.findAllByContaId(id);
	}

}