package com.accenture.academico.bankapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="clientes")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="idCliente")
	private Long id;
	
	@Column(name="clienteNome")
	private String nome;
	
	@Column(name="clienteCpf")
	private String cpf;
	
	@Column(name="clienteFone")
	private String telefone;
	
		
}
