package br.com.bruno.toshiaki.servicos;

import static br.com.bruno.toshiaki.utils.DataUtils.isMesmaData;
import static br.com.bruno.toshiaki.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import br.com.bruno.toshiaki.entidades.Filme;
import br.com.bruno.toshiaki.entidades.Locacao;
import br.com.bruno.toshiaki.entidades.Usuario;
import br.com.bruno.toshiaki.exceptions.FilmeSemEstoqueException;
import br.com.bruno.toshiaki.exceptions.LocadoraException;
import br.com.bruno.toshiaki.servico.LocacaoService;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

public class LocacaoServiceTest {

  @Rule
  public ErrorCollector error = new ErrorCollector();
  @Rule
  public ExpectedException exception = ExpectedException.none();
  private LocacaoService service;

  @Before
  public void setup() {
    service = new LocacaoService();
  }

  @Test
  public void deveAlugarFilme() throws Exception {
    //cenario
    var usuario = new Usuario("Usuario 1");
    var filmes = List.of(new Filme("Filme 1", 1, 5.0));

    //acao
    var locacao = service.alugarFilme(usuario, filmes);

    //verificacao
    error.checkThat(locacao.getValor(), is(equalTo(5.0)));
    error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
    error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
  }

  @Test(expected = FilmeSemEstoqueException.class)
  public void naoDeveAlugarFilmeSemEstoque() throws Exception {
    //cenario
    var usuario = new Usuario("Usuario 1");
    var filmes = List.of(new Filme("Filme 1", 0, 4.0));

    //acao
    service.alugarFilme(usuario, filmes);
  }

  @Test
  public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
    //cenario
    var filmes = List.of(new Filme("Filme 1", 1, 5.0));

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
    var usuario = new Usuario("Usuario 1");

    exception.expect(LocadoraException.class);
    exception.expectMessage("Filme vazio");

    //acao
    service.alugarFilme(usuario, null);
  }

  @Test
  public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    var usuario = new Usuario("Usuario 1");
    var filmes = List.of(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
        new Filme("Filme 3", 2, 4.0));

    //acao
    Locacao resultado = service.alugarFilme(usuario, filmes);

    //verificacao
    assertThat(resultado.getValor(), is(11.0));
  }


  @Test
  public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    var usuario = new Usuario("Usuario 1");
    var filmes = List.of(
        new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0),
        new Filme("Filme 4", 2, 4.0));

    //acao
    Locacao resultado = service.alugarFilme(usuario, filmes);

    //verificacao
    assertThat(resultado.getValor(), is(13.0));
  }


  @Test
  public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario 1");
    var filmes = List.of(
        new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
        new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0),
        new Filme("Filme 5", 2, 4.0));

    //acao
    Locacao resultado = service.alugarFilme(usuario, filmes);

    //verificacao
    assertThat(resultado.getValor(), is(14.0));
  }


  @Test
  public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario 1");
    var filmes = List.of(
        new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
        new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0),
        new Filme("Filme 5", 2, 4.0), new Filme("Filme 6", 2, 4.0));

    //acao
    Locacao resultado = service.alugarFilme(usuario, filmes);

    //verificacao
    assertThat(resultado.getValor(), is(14.0));
  }
}
