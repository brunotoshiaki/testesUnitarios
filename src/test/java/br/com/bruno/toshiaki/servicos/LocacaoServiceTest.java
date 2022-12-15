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
  public void testeLocacao() throws Exception {
    //cenario
    Usuario usuario = new Usuario("Usuario 1");
    Filme filme = new Filme("Filme 1", 1, 5.0);

    //acao
    Locacao locacao = service.alugarFilme(usuario, filme);

    //verificacao
    error.checkThat(locacao.getValor(), is(equalTo(5.0)));
    error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
    error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
  }

  @Test(expected = FilmeSemEstoqueException.class)
  public void testLocacao_filmeSemEstoque() throws Exception {
    //cenario
    Usuario usuario = new Usuario("Usuario 1");
    Filme filme = new Filme("Filme 2", 0, 4.0);

    //acao
    service.alugarFilme(usuario, filme);
  }

  @Test
  public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {
    //cenario
    Filme filme = new Filme("Filme 2", 1, 4.0);

    //acao
    try {
      service.alugarFilme(null, filme);
      Assert.fail();
    } catch (LocadoraException e) {
      assertThat(e.getMessage(), is("Usuario vazio"));
    }

  }


  @Test
  public void testLocacao_FilmeVazio() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario 1");

    exception.expect(LocadoraException.class);
    exception.expectMessage("Filme vazio");

    //acao
    service.alugarFilme(usuario, null);
  }
}
