package com.accenture.academico.bankapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.accenture.academico.bankapi.model.Extrato;

public interface ExtratoRepository extends JpaRepository<Extrato, Long> {

	public List<Extrato> findAllByContaId(Long id);

}
