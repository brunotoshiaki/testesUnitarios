package br.com.bruno.toshiaki.servico;


import br.com.bruno.toshiaki.exceptions.NaoPodeDividirPorZeroException;

public class Calculadora {

  public static void main(String[] args) {
    new Calculadora().divide("a", "b");
  }

  public int somar(int a, int b) {
    System.out.println("Passei no somar");
    return a + b;
  }

  public int subtrair(int a, int b) {
    return a - b;
  }

  public int divide(int a, int b) throws NaoPodeDividirPorZeroException {
    if (b == 0) {
      throw new NaoPodeDividirPorZeroException();
    }
    return a / b;
  }

  public int divide(String a, String b) {
    return Integer.parseInt(a) / Integer.parseInt(b);
  }

  public void imprime() {
    System.out.println("imprimindo");
  }

}
