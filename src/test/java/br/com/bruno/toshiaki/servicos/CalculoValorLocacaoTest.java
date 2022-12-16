package br.com.bruno.toshiaki.servicos;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import br.com.bruno.toshiaki.entidades.Filme;
import br.com.bruno.toshiaki.entidades.Usuario;
import br.com.bruno.toshiaki.exceptions.FilmeSemEstoqueException;
import br.com.bruno.toshiaki.exceptions.LocadoraException;
import br.com.bruno.toshiaki.servico.LocacaoService;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

  private static final Filme filme1 = new Filme("Filme 1", 2, 4.0);
  private static final Filme filme2 = new Filme("Filme 2", 2, 4.0);
  private static final Filme filme3 = new Filme("Filme 3", 2, 4.0);
  private static final Filme filme4 = new Filme("Filme 4", 2, 4.0);
  private static final Filme filme5 = new Filme("Filme 5", 2, 4.0);
  private static final Filme filme6 = new Filme("Filme 6", 2, 4.0);
  private static final Filme filme7 = new Filme("Filme 7", 2, 4.0);
  @Parameter
  public List<Filme> filmes;
  @Parameter(value = 1)
  public Double valorLocacao;
  @Parameter(value = 2)
  public String cenario;
  private LocacaoService service;

  @Parameters(name = "{2}")
  public static Collection<Object[]> getParametros() {
    return List.of(new Object[][]{
        {List.of(filme1, filme2), 8.0, "2 Filmes: Sem Desconto"},
        {List.of(filme1, filme2, filme3), 11.0, "3 Filmes: 25%"},
        {List.of(filme1, filme2, filme3, filme4), 13.0, "4 Filmes: 50%"},
        {List.of(filme1, filme2, filme3, filme4, filme5), 14.0, "5 Filmes: 75%"},
        {List.of(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6 Filmes: 100%"},
        {List.of(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0,
            "7 Filmes: Sem Desconto"}
    });
  }

  @Before
  public void setup() {
    service = new LocacaoService();
  }

  @Test
  public void deveCalcularValorLocacaoConsiderandoDescontos()
      throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    var usuario = new Usuario("Usuario 1");

    //acao
    var resultado = service.alugarFilme(usuario, filmes);

    //verificacao
    assertThat(resultado.getValor(), is(valorLocacao));
  }
}
