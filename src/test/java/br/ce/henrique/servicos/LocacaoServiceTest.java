package br.ce.henrique.servicos;



import static br.ce.henrique.utils.DataUtils.isMesmaData;
import static br.ce.henrique.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import br.ce.henrique.daos.LocacaoDAO;
import br.ce.henrique.daos.LocacaoDAOFake;
import br.ce.henrique.entidades.Filme;
import br.ce.henrique.entidades.Locacao;
import br.ce.henrique.entidades.Usuario;
import br.ce.henrique.exceptions.FilmeSemEstoqueException;
import br.ce.henrique.exceptions.LocadoraException;
import br.ce.henrique.matchers.MatchersProprios;
import br.ce.henrique.utils.DataUtils;




public class LocacaoServiceTest {
	
	private LocacaoService service;
	
	private SPCService spc;
	private LocacaoDAO dao;
	
    
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() {
		service = new LocacaoService();
		 
		
		 //uso do Fake
		 //LocacaoDAO dao = new LocacaoDAOFake();
		 
		 //uso do mockito
		 dao = Mockito.mock(LocacaoDAO.class);
		 service.setLocacaoDAO(dao);
		 
		 spc = Mockito.mock(SPCService.class);
		 service.setSPCService(spc);
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
     
     @Test
     public void naoDeveAlugarFilmesParaNegativadoSPC() throws FilmeSemEstoqueException, LocadoraException {
    	 //cenario
    	 Usuario usuario = new Usuario("Ususario 1");
    	 List<Filme> filmes = Arrays.asList(new Filme("1", 1, 5.0));
    	 
    	 when(spc.possuiNegativacao(usuario)).thenReturn(true);
    	 
    	 exception.expect(LocadoraException.class);
    	 exception.expectMessage("Usuario Negativado");
    	 
    	 //acao
    	 service.alugarFilme(usuario, filmes);
    	 
     }
     
  

	


}
