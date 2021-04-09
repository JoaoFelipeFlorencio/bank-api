package com.accenture.academico.bankapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.academico.bankapi.model.ContaCorrente;

@Repository
public interface ContaCorrenteRepository  extends JpaRepository<ContaCorrente, Long>{

	public ContaCorrente findTop1ByNumero(String numero);
	
	public List<ContaCorrente> findAllByClienteId(Long id);
	
}