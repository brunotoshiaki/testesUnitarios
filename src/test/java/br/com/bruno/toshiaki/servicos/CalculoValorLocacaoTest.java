package br.com.bruno.toshiaki.servicos;


import static br.com.bruno.toshiaki.builders.FilmeBuilder.umFilme;
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

  private static final Filme filme1 = umFilme().agora();
  private static final Filme filme2 = umFilme().agora();
  private static final Filme filme3 = umFilme().agora();
  private static final Filme filme4 = umFilme().agora();
  private static final Filme filme5 = umFilme().agora();
  private static final Filme filme6 = umFilme().agora();
  private static final Filme filme7 = umFilme().agora();
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
