package com.accenture.academico.bankapi.service;


import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.accenture.academico.bankapi.model.ContaCorrente;
import com.accenture.academico.bankapi.repository.ContaCorrenteRepository;


@Service
public class ContaCorrenteService {

	@Autowired
	private ContaCorrenteRepository repository;
	
	public List<ContaCorrente> buscarContas(){
		return this.repository.findAll();
	}
	
	public ContaCorrente buscarConta(Long id) {
		return this.repository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao pesquisar a conta"));
	}
	
	public void criarConta(ContaCorrente conta) {
		this.checkInputs(conta);
		this.numeroIsRegistred(conta.getNumero());
		conta.setSaldo(0);
		
		try {
			this.repository.save(conta);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro encontrado. Verifique o id do cliente e da agencia");
		}
	}
	
	public void atualizarConta(Long id, ContaCorrente conta) {
		ContaCorrente contaBanco = this.buscarConta(id);
		conta.setId(null);
		conta.setCliente(null);
		conta.setAgencia(null);
		
		if(conta.getNumero() != null && !(conta.getNumero().equals(contaBanco.getNumero()))) {
			this.numeroIsRegistred(conta.getNumero());
		}
		
		BeanUtils.copyProperties(conta,contaBanco, getNullPropertyNames(conta));
		this.checkInputs(contaBanco);
		this.repository.save(contaBanco);
	}
	
	public void removerConta(Long id) {
		ContaCorrente contaBanco = this.buscarConta(id);
		this.repository.delete(contaBanco);
	}
	
	
	private void checkInputs (ContaCorrente conta) {
		if(conta.getNumero()=="" || conta.getNumero()==null || conta.getAgencia().getId()==null || conta.getCliente().getId()==null) {
			throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "Campos em branco ou nulos");
		}
		
	}
	
	private void numeroIsRegistred(String numero) {
		if(this.repository.findTop1ByNumero(numero) != null) {
			throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "Numero de conta ja cadastrado");
		}
	}
	
	private String[] getNullPropertyNames(Object source) {
	    final BeanWrapper src = new BeanWrapperImpl(source);
	    PropertyDescriptor[] pds = src.getPropertyDescriptors();
	    
	    Set<String> emptyNames = new HashSet<String>();
	    emptyNames.add("saldo");
	    for (PropertyDescriptor pd : pds) {
	        Object srcValue = src.getPropertyValue(pd.getName());
	        if (srcValue == null)
	            emptyNames.add(pd.getName());
	    }
	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}
	
}
