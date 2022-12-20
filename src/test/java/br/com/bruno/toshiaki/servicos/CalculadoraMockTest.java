package br.com.bruno.toshiaki.servicos;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import br.com.bruno.toshiaki.servico.Calculadora;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


public class CalculadoraMockTest {

  @Mock
  private Calculadora calcMock;

  @Spy
  private Calculadora calcSpy;


  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void deveMostrarDiferencaEntreMockSpy() {
    this.mock();
    this.spy();
    assertTrue(true);
  }

  private void mock() {
    //mockito coloca o valor padrao como retorno
    System.out.println("Mockito sem definir retorno: " + this.calcMock.somar(1, 2));

    //definindo o retorno
    when(this.calcMock.somar(1, 2)).thenReturn(8);

    System.out.println("Mockito definindo o retorno: " + this.calcMock.somar(1, 2));

    //se mudar o parametro ele sempre retornara o valor padrao

    when(this.calcMock.somar(1, 5)).thenReturn(8);
    System.out.println("Mockito mudando o segundo parametro (2 foi substituido por 5): " + this.calcMock.somar(1, 2));
  }

  /**
   * O SPY sempre retorna a execucao do metodo
   */
  private void spy() {
    System.out.println("--- SPY ---");
    System.out.println("SPY sem definir retorno: " + this.calcSpy.somar(1, 2));

    when(this.calcSpy.somar(1, 2)).thenReturn(8);
    System.out.println("SPY com retorno: " + this.calcSpy.somar(1, 2));

    when(this.calcSpy.somar(1, 5)).thenReturn(8);
    System.out.println("SPY mudando o segundo parametro (2 foi substituido por 5): " + this.calcSpy.somar(1, 2));
  }


  @Test
  public void mockDeveChamarMetodoReal() {
    when(this.calcMock.somar(1, 2)).thenCallRealMethod();
    when(this.calcSpy.somar(1, 2)).thenReturn(8);
    System.out.println("Mock: " + this.calcMock.somar(1, 2));
    System.out.println("SPY: " + this.calcSpy.somar(1, 2));
    assertTrue(true);

  }

  @Test
  public void teste() {
    final var calc = Mockito.mock(Calculadora.class);

    final ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
    //capturando parametros passado via argumento
    when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);

    Assert.assertEquals(5, calc.somar(134345, -234));
    System.out.println(argCapt.getAllValues());
  }

  @Test
  public void comportamentoComMetodosVoid() {
    System.out.println("Mock: ");
    this.calcMock.imprime();
    System.out.println("Spy: ");
    this.calcSpy.imprime();
    assertTrue(true);
  }

  @Test
  public void testandoMetodosVoids() {
    doNothing().when(this.calcSpy).imprime();
    System.out.println("Mock: ");
    this.calcMock.imprime();
    System.out.println("Spy: ");
    this.calcSpy.imprime();
    assertTrue(true);
  }

  @Test
  public void testandoComportamentoNormal() {
    when(this.calcMock.somar(1, 2)).thenReturn(5);
    //nao fez a chamada
    when(this.calcSpy.somar(1, 2)).thenReturn(5);
    System.out.println("Mock: " + this.calcMock.somar(1, 2));
    System.out.println("SPY: " + this.calcSpy.somar(1, 2));
    assertTrue(true);

  }

  @Test
  public void testandoComportamentoComDoReturn() {
    when(this.calcMock.somar(1, 2)).thenReturn(5);
    doReturn(5).when(calcSpy).somar(1, 2);
    System.out.println("Mock: " + this.calcMock.somar(1, 2));
    System.out.println("SPY: " + this.calcSpy.somar(1, 2));
    assertTrue(true);
  }
}
