package com.accenture.academico.bankapi.service;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.accenture.academico.bankapi.model.Extrato;
import com.accenture.academico.bankapi.repository.ContaCorrenteRepository;
import com.accenture.academico.bankapi.util.Operacoes;

@Service
public class ContaCorrenteService {

	@Autowired
	private ContaCorrenteRepository repository;
	@Autowired
	private ExtratoService extratoService;
	private DateTimeFormatter dateFormat;

	public ContaCorrenteService() {
		this.dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	}

	public List<ContaCorrente> buscarContas() {
		return this.repository.findAll();
	}

	public ContaCorrente buscarConta(Long id) {
		return this.repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao pesquisar a conta"));
	}

	public void criarConta(ContaCorrente conta) {
		this.checkInputs(conta);
		this.numeroIsRegistred(conta.getNumero());
		conta.setSaldo(0);

		try {
			this.repository.save(conta);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Erro encontrado. Verifique o id do cliente e da agencia");
		}
	}

	public void atualizarConta(Long id, ContaCorrente conta) {
		ContaCorrente contaBanco = this.buscarConta(id);
		conta.setId(null);
		conta.setCliente(null);
		conta.setAgencia(null);

		if (conta.getNumero() != null && !(conta.getNumero().equals(contaBanco.getNumero()))) {
			this.numeroIsRegistred(conta.getNumero());
		}

		BeanUtils.copyProperties(conta, contaBanco, getNullPropertyNames(conta));
		this.checkInputs(contaBanco);
		this.repository.save(contaBanco);
	}

	public void removerConta(Long id) {
		ContaCorrente contaBanco = this.buscarConta(id);
		this.repository.delete(contaBanco);
	}

	public void depositarValor(Long id, Extrato operacao) {
		ContaCorrente contaBanco = this.buscarConta(id);
		if (operacao.getValor() <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor da operacao e invalido");
		}
		contaBanco.setSaldo(contaBanco.getSaldo() + operacao.getValor());
		this.repository.save(contaBanco);
		operacao.setConta(contaBanco);
		operacao.setOperacao(Operacoes.DEPOSITO.toString());
		operacao.setDate(dateFormat.format(LocalDateTime.now()));
		this.extratoService.inserirExtrato(operacao);
	}

	public void sacarValor(Long id, Extrato operacao) {
		ContaCorrente contaBanco = this.buscarConta(id);
		if (operacao.getValor() <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor da operacao e invalido");
		}
		if (operacao.getValor() > contaBanco.getSaldo()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente");
		}
		contaBanco.setSaldo(contaBanco.getSaldo() - operacao.getValor());
		this.repository.save(contaBanco);
		operacao.setConta(contaBanco);
		operacao.setOperacao(Operacoes.SAQUE.toString());
		operacao.setValor(operacao.getValor() * -1);
		operacao.setDate(dateFormat.format(LocalDateTime.now()));
		this.extratoService.inserirExtrato(operacao);
	}

	public void transferirValor(Long id, Long idDestino, Extrato operacao) {
		ContaCorrente contaOrigem = this.buscarConta(id);
		ContaCorrente contaDestino = this.repository.findById(idDestino)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Nao foi possivel encontrar a conta destino"));
		if (operacao.getValor() <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor da operacao e invalido");
		}
		if (operacao.getValor() > contaOrigem.getSaldo()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente");
		}
		contaOrigem.setSaldo(contaOrigem.getSaldo() - operacao.getValor());
		contaDestino.setSaldo(contaDestino.getSaldo() + operacao.getValor());
		this.repository.save(contaOrigem);
		this.repository.save(contaDestino);

		Extrato extratoDestino = new Extrato();
		extratoDestino.setValor(operacao.getValor());
		extratoDestino.setOperacao(Operacoes.TRANSFERENCIA_RECEBIDA.toString());
		extratoDestino.setConta(contaDestino);
		extratoDestino.setDate(dateFormat.format(LocalDateTime.now()));
		this.extratoService.inserirExtrato(extratoDestino);

		operacao.setConta(contaOrigem);
		operacao.setOperacao(Operacoes.TRANSFERENCIA.toString());
		operacao.setValor(operacao.getValor() * -1);
		operacao.setDate(dateFormat.format(LocalDateTime.now()));
		this.extratoService.inserirExtrato(operacao);

	}

	public void recalcularSaldo(Long id) {
		ContaCorrente contaBanco = this.buscarConta(id);
		List<Extrato> extrato = this.extratoService.buscarExtrato(id);
		if (!extrato.isEmpty()) {
			double saldo = 0;
			for (Extrato operacao : extrato) {
				saldo = saldo + operacao.getValor();
			}
			contaBanco.setSaldo(saldo);
			this.repository.save(contaBanco);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Transacoes ainda nao foram realizadas nesta conta");
		}
	}

	public List<ContaCorrente> buscarContasCliente(Long id) {
		return this.repository.findAllByClienteId(id);
	}

	private void checkInputs(ContaCorrente conta) {
		if (conta.getNumero() == "" || conta.getNumero() == null || conta.getAgencia().getId() == null
				|| conta.getCliente().getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campos em branco ou nulos");
		}

	}

	private void numeroIsRegistred(String numero) {
		if (this.repository.findTop1ByNumero(numero) != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Numero de conta ja cadastrado");
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
