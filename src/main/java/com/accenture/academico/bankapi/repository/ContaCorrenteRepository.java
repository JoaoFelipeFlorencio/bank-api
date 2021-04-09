package com.accenture.academico.bankapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.accenture.academico.bankapi.model.ContaCorrente;


public interface ContaCorrenteRepository  extends JpaRepository<ContaCorrente, Long>{

	public ContaCorrente findTop1ByNumero(String numero);
	
	public List<ContaCorrente> findAllByClienteId(Long id);
	
}