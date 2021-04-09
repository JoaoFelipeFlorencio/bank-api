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

import com.accenture.academico.bankapi.model.Agencia;
import com.accenture.academico.bankapi.repository.AgenciaRepository;
import com.accenture.academico.bankapi.util.Validador;



@Service
public class AgenciaService {
	@Autowired
	private AgenciaRepository  repository;
	
	
	public List<Agencia> buscarAgencias(){
		return repository.findAll();
	}
	
	public void salvarAgencia(Agencia agencia) {
		this.checkInputs(agencia);
		Validador.isNumeroValid(agencia.getNumero());
		this.numeroIsRegistred(agencia.getNumero());
		repository.save(agencia);
	}

	public void atualizarAgencia(long id, Agencia agencia) {
		Agencia agenciaBanco = this.buscarAgencia(id);
		agencia.setId(null);
		
		if(agencia.getNumero() !=null && !(agencia.getNumero().equals(agenciaBanco.getNumero()))) {
			Validador.isNumeroValid(agencia.getNumero());
			numeroIsRegistred(agencia.getNumero());
		}
		
		BeanUtils.copyProperties(agencia, agenciaBanco, this.getNullPropertyNames(agencia));
		checkInputs(agenciaBanco);
		this.repository.save(agenciaBanco);
	}

	public void removerAgencia(Long id ) {
		Agencia agenciaBanco = this.buscarAgencia(id);
		try {
		 this.repository.delete(agenciaBanco);
		}catch(Exception e) {
			throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "Agencia ainda possui contas ativas");
		}
	}
	
	public Agencia buscarAgencia(Long id) {
		return this.repository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Erro ao pesquisar a agencia"));
	}
	
	private void checkInputs (Agencia agencia) {
		if(agencia.getNumero().trim()=="" || agencia.getNumero()==null || agencia.getEndereco().trim()=="" || agencia.getEndereco()==null || agencia.getTelefone().trim()==""|| agencia.getTelefone()==null) {
			throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "Campos em branco ou nulos");
		}
		
	}
	
	private void numeroIsRegistred(String numero) {
		if(this.repository.findTop1ByNumero(numero) != null) {
			throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "Numero de conta ja cadastrado");
		}
	}
	
	public String[] getNullPropertyNames(Object source) {
	    final BeanWrapper src = new BeanWrapperImpl(source);
	    PropertyDescriptor[] pds = src.getPropertyDescriptors();
	    
	    Set<String> emptyNames = new HashSet<String>();
	    for (PropertyDescriptor pd : pds) {
	        Object srcValue = src.getPropertyValue(pd.getName());
	        if (srcValue == null)
	            emptyNames.add(pd.getName());
	    }
	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}
}