package exemploTest;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;

import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdemTest {
	
	private static int contador = 0;
	@Test
	public void inicia() {
		contador = 1;
	}
	@Test
	public void verifica() {
		assertEquals(1, contador);
	}
	
	

}
