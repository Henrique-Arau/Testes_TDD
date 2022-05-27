package br.ce.henrique.servicos;



import static br.ce.henrique.utils.DataUtils.isMesmaData;
import static br.ce.henrique.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.henrique.entidades.Filme;
import br.ce.henrique.entidades.Locacao;
import br.ce.henrique.entidades.Usuario;
import br.ce.henrique.exceptions.FilmeSemEstoqueException;
import br.ce.henrique.exceptions.LocadoraException;
import br.ce.henrique.matchers.DiaSemanaMatcher;
import br.ce.henrique.matchers.MatchersProprios;
import br.ce.henrique.servicos.LocacaoService;
import br.ce.henrique.utils.DataUtils;



public class LocacaoServiceTest {
	
	private LocacaoService service;
	//definicao do contador
	private static int contador = 0;
    
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() {
		System.out.println("Before");
		service = new LocacaoService();
		//incremento
		contador++;
		//impressão do contador
		System.out.println(contador);
	}
    
	@After
	public void tearDown() {
		System.out.println("After");
		
	}
	@BeforeClass
	public static void setUpClass() {
		System.out.println("Before class");
	}
    
	@AfterClass
	public static void tearDownClass() {
		System.out.println("After class");
		
	}
    @Test
	public void deveAlugarFilme() throws Exception{
    	
    	Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));
		
		System.out.println("Before");
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		
		
		//verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(locacao.getDataLocacao(), MatchersProprios.ehHoje());
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		error.checkThat(locacao.getDataRetorno(),  MatchersProprios.ehHojeComDiferencaDias(1));
		
	}
		
     @Test(expected= FilmeSemEstoqueException.class)
     public void naodeveAlugarFilmeSemEstoque() throws Exception {
    	 
        //cenario
 		Usuario usuario = new Usuario("Usuario 1");
 		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));
 		
 		System.out.println("After");
 		
 		//Acao
 		 service.alugarFilme(usuario, filmes);
 		
    	 
     }
     
     @Test
     public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
    	 //cenario
    	 List<Filme> filmes = Arrays.asList(new Filme("Filme 2", 1, 5.0));
    	 
    	 //acao
    	 try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
	       assertThat(e.getMessage(), is("Usuario vazio"));
		}
     }
     
     @Test
     public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
    	 
         //cenario
  		Usuario usuario = new Usuario("Usuario 1");
  		
  		 exception.expect(LocadoraException.class);
  		 exception.expectMessage("Filme vazio");
  		//Acao
		 service.alugarFilme(usuario, null);
		 
    	 
     }
     
     @Test
     public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
    	 
    	 //cenario
    	 Usuario usuario = new Usuario("Usuario 1");
    	 List<Filme> filmes = Arrays.asList(
    			 new Filme("Filme 1", 2, 4.0), 
    			 new Filme("Filme 2", 2, 4.0), 
    			 new Filme("Filme 3", 2, 4.0));
    	 
    	 //acao
    	 Locacao resultado = service.alugarFilme(usuario, filmes);
    	 
    	 //verificacao
    	 //4+4+3=11
    	 assertThat(resultado.getValor(), is(11.0));
     }
     
     @Test
     public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
    	 
    	 //cenario
    	 Usuario usuario = new Usuario("Usuario 1");
    	 List<Filme> filmes = Arrays.asList(
    			 new Filme("Filme 1", 2, 4.0), 
    			 new Filme("Filme 2", 2, 4.0), 
    			 new Filme("Filme 3", 2, 4.0),
    			 new Filme("Filme 4", 2, 4.0));
    	 
    	 //acao
    	 Locacao resultado = service.alugarFilme(usuario, filmes);
    	 
    	 //verificacao
    	 //4+4+3+2=13
    	 assertThat(resultado.getValor(), is(13.0));
     }
     
     @Test
     public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
    	 
    	 //cenario
    	 Usuario usuario = new Usuario("Usuario 1");
    	 List<Filme> filmes = Arrays.asList(
    			 new Filme("Filme 1", 2, 4.0), 
    			 new Filme("Filme 2", 2, 4.0), 
    			 new Filme("Filme 3", 2, 4.0),
    			 new Filme("Filme 4", 2, 4.0),
    			 new Filme("Filme 5", 2, 4.0));
    	 
    	 //acao
    	 Locacao resultado = service.alugarFilme(usuario, filmes);
    	 
    	 //verificacao
    	 //4+4+3+2+1=14
    	 assertThat(resultado.getValor(), is(14.0));
     }
     
     @Test
     public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
    	 
    	 //cenario
    	 Usuario usuario = new Usuario("Usuario 1");
    	 List<Filme> filmes = Arrays.asList(
    			 new Filme("Filme 1", 2, 4.0), 
    			 new Filme("Filme 2", 2, 4.0), 
    			 new Filme("Filme 3", 2, 4.0),
    			 new Filme("Filme 4", 2, 4.0),
    			 new Filme("Filme 5", 2, 4.0),
    			 new Filme("Filme 6", 2, 4.0));
    	 
    	 //acao
    	 Locacao resultado = service.alugarFilme(usuario, filmes);
    	 
    	 //verificacao
    	 //4+4+3+2+1+0=14
    	 assertThat(resultado.getValor(), is(14.0));
     }
     
     @Test
     //Teste só funciona no sabado
     public void devoDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
    	 
    	 Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
    	 
    	 
    	 
    	 //cenario
    	 Usuario usuario = new Usuario("Ususario 1");
    	 List<Filme> filmes = Arrays.asList(new Filme("1", 1, 5.0));
    	 
    	 //acao
    	 Locacao retorno = service.alugarFilme(usuario, filmes);
    	 
    	 //verificacao
    	 boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
    	 assertTrue(ehSegunda);
       // assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
       //assertThat(retorno.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
       assertThat(retorno.getDataRetorno(), MatchersProprios.caiNumaSegunda());
    	 
     }
     
     /*
     
     @Test
     public void testLocacao_filmeSemEstoque_2() {
    	 
        //cenario
 		LocacaoService service= new LocacaoService();
 		Usuario usuario = new Usuario("Usuario 1");
 		Filme filme = new Filme("Filme 1", 2, 5.0);
 		
 		//Acao
 		 try {
			service.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lancado uma excecao");
		} catch (Exception e) {
		  assertThat(e.getMessage(), is("Filme sem estoque"));
		}
 		  
 		
    	 
     }*/
     

	

		//verificacao
		//System.out.println(locacao.getValor() == 5.0);
		//System.out.println(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		//System.out.println(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
        
        //exemplo assertThat
        
        //Assert.assertThat(locacao.getValor(), is(equalTo(5.0)));
        //Assert.assertThat(locacao.getValor(), is(not(6.0)));
        //Assert.assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true)); erro
        //Assert.assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true)); erro
		
		/*acao
        Locacao locacao;
		try {
			locacao = service.alugarFilme(usuario, filme);
			
			//verificacao
			error.checkThat(locacao.getValor(), is(equalTo(5.0)));
			error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
			error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Não deveria lancar excecao");
		}*/
 

    

	


}
