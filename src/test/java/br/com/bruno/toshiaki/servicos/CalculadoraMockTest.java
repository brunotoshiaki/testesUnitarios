package br.com.bruno.toshiaki.servicos;

import br.com.bruno.toshiaki.servico.Calculadora;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;


public class CalculadoraMockTest {

	@Test
	public void teste() {
		var calc = Mockito.mock(Calculadora.class);

		ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
		//capturando parametros passado via argumento
		Mockito.when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);

		Assert.assertEquals(5, calc.somar(134345, -234));
		System.out.println(argCapt.getAllValues());
	}
}
