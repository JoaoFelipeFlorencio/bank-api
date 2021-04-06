package com.accenture.academico.bankapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.academico.bankapi.model.Agencia;



@Repository
public interface AgenciaRepository extends JpaRepository<Agencia, Long>{

	
	public Agencia findTop1ByNumero(String numero);
}
