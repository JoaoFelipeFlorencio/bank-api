package com.accenture.academico.bankapi.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Validador {
	
	private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

	private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice=str.length()-1, digito; indice >= 0; indice-- ) {
            digito = Integer.parseInt(str.substring(indice,indice+1));
            soma += digito*peso[peso.length-str.length()+indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }
	
	 private static String padLeft(String text, char character) {
	        return String.format("%11s", text).replace(' ', character);
	    }
	
	public static void isCpf(String cpf) {
		if(!(cpf.matches("[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}\\-[0-9]{2}"))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF em formato invalido. ex: 999.999.999-99");
		}
		
		cpf = cpf.trim().replace(".", "").replace("-", "");

        for (int j = 0; j < 10; j++)
            if (padLeft(Integer.toString(j), Character.forDigit(j, 10)).equals(cpf))
            	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF invalido");

        Integer digito1 = calcularDigito(cpf.substring(0,9), pesoCPF);
        Integer digito2 = calcularDigito(cpf.substring(0,9) + digito1, pesoCPF);
        if (!cpf.equals(cpf.substring(0,9) + digito1.toString() + digito2.toString()))
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF invalido");
	}
	
}