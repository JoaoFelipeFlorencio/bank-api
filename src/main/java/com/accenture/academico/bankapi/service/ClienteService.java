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

import com.accenture.academico.bankapi.model.Cliente;
import com.accenture.academico.bankapi.repository.ClienteRepository;
import com.accenture.academico.bankapi.util.Validador;




@Service
public class ClienteService {

	@Autowired
	private ClienteRepository  repository;
	
	
	public List<Cliente> buscarClientes(){
		return repository.findAll();
	}
	
	public void salvarCliente(Cliente cliente) {
		checkInputs(cliente);
		Validador.isCpf(cliente.getCpf());
		cpfIsRegistred(cliente.getCpf());
		repository.save(cliente);
	}

	public void atualizarCliente(long id, Cliente cliente) {
		Cliente clienteBanco = this.buscarCliente(id);
		cliente.setId(null);
		if(cliente.getCpf()!=null && !(cliente.getCpf().equals(clienteBanco.getCpf()))) {
			Validador.isCpf(cliente.getCpf());
			cpfIsRegistred(cliente.getCpf());
		}
			
		BeanUtils.copyProperties(cliente, clienteBanco, this.getNullPropertyNames(cliente));
		checkInputs(clienteBanco);
		repository.save(clienteBanco);
	}

	public void removerCliente(Long id ) {
		Cliente clienteBanco = this.buscarCliente(id);
		try {
			this.repository.delete(clienteBanco);
		}catch(Exception e) {
			throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente ainda possui contas ativas");
		}
	}
	
	public Cliente buscarCliente(Long id) {
		return this.repository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Erro ao pesquisar o cliente"));
	}
	
	private void checkInputs (Cliente cliente) {
		if(cliente.getCpf().trim()=="" || cliente.getCpf()==null || cliente.getNome().trim()=="" || cliente.getNome()==null || cliente.getTelefone().trim()==""|| cliente.getTelefone()==null) {
			throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "Campos em branco ou nulos");
		}
		
	}
	
	private void cpfIsRegistred(String cpf) {
		if(this.repository.findTop1ByCpf(cpf) != null) {
			throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF ja cadastrado");
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
