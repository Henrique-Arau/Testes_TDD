package br.ce.henrique.servicos;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.ce.henrique.exceptions.NaoPodeDividirPorZeroException;
import br.ce.henrique.servicos.Calculadora;

public class CalculadoraTest {

	
	
	@Test
	public void deveSomarDoisValores() {
		
		//cenario
		int a = 5;
		int b = 3;
		Calculadora calc = new Calculadora();
		
		//acao
		int resultado = calc.soma(a, b);
		
		//verificacao
		assertEquals(8, resultado);
	  
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		
		//cenario
		 int a = 8;
		 int b = 5;
		 Calculadora calc = new Calculadora();
		 
		//acao
		int resultado = calc.subtrair(a, b);
		
		//verificacao
		assertEquals(3, resultado);
		
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		//cenario
		 int a = 8;
		 int b = 2;
		 Calculadora calc = new Calculadora();
		 
		//acao
		int resultado = calc.dividir(a, b);
		
		//verificacao
		assertEquals(4, resultado);
		
		
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		//cenario
		 int a = 10;
		 int b = 0;
		 Calculadora calc = new Calculadora();
		 
		//acao
		calc.dividir(a, b);
		
		
	}
}
