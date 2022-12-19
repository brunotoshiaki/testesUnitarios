package br.com.bruno.toshiaki.servicos;

import static br.com.bruno.toshiaki.builders.FilmeBuilder.umFilme;
import static br.com.bruno.toshiaki.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.com.bruno.toshiaki.builders.UsuarioBuilder.umUsuario;
import static br.com.bruno.toshiaki.matchers.MatchersProprios.caiNumaSegunda;
import static br.com.bruno.toshiaki.matchers.MatchersProprios.ehHoje;
import static br.com.bruno.toshiaki.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import br.com.bruno.toshiaki.daos.LocacaoDao;
import br.com.bruno.toshiaki.exceptions.FilmeSemEstoqueException;
import br.com.bruno.toshiaki.exceptions.LocadoraException;
import br.com.bruno.toshiaki.servico.LocacaoService;
import br.com.bruno.toshiaki.utils.DataUtils;
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

public class LocacaoServiceTest {

  @Rule
  public ErrorCollector error = new ErrorCollector();
  @Rule
  public ExpectedException exception = ExpectedException.none();
  private LocacaoService service;

  @Before
  public void setup() {
    service = new LocacaoService();
    var dao = mock(LocacaoDao.class);
    service.setLocacaoDao(dao);
  }

  @Test
  public void deveAlugarFilme() throws Exception {
    Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

    //cenario
    var usuario = umUsuario().agora();
    var filmes = List.of(umFilme().comValor(5.0).agora());

    //acao
    var locacao = service.alugarFilme(usuario, filmes);

    //verificacao
    error.checkThat(locacao.getValor(), is(equalTo(5.0)));
    error.checkThat(locacao.getDataLocacao(), ehHoje());
    error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
  }

  @Test(expected = FilmeSemEstoqueException.class)
  public void naoDeveAlugarFilmeSemEstoque() throws Exception {
    //cenario
    var usuario = umUsuario().agora();
    var filmes = List.of(umFilmeSemEstoque().agora());

    //acao
    service.alugarFilme(usuario, filmes);
  }

  @Test
  public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
    //cenario
    var filmes = List.of(umFilme().agora());

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
    var usuario = umUsuario().agora();

    exception.expect(LocadoraException.class);
    exception.expectMessage("Filme vazio");

    //acao
    service.alugarFilme(usuario, null);
  }


  @Test
  public void deveDevolverSegundaAlugandoSabado()
      throws FilmeSemEstoqueException, LocadoraException {

    Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

    //cenario
    var usuario = umUsuario().agora();
    var filmes = List.of(umFilme().agora());

    //acao
    var retorno = service.alugarFilme(usuario, filmes);

    //verificacao

    assertThat(retorno.getDataRetorno(), caiNumaSegunda());

  }
}
