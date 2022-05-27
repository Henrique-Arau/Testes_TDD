package br.ce.henrique.suite;

import org.junit.runners.Suite.SuiteClasses;

import br.ce.henrique.servicos.CalculadoraTest;
import br.ce.henrique.servicos.CalculoValorLocacaoTest;
import br.ce.henrique.servicos.LocacaoServiceTest;

//@RunWith(Suite.class)
@SuiteClasses({
	
	CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTest.class
})
public class SuiteExecucao {
	//Remova se poder!
	
	
	
}

