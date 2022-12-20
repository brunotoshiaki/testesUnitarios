package br.com.bruno.toshiaki.servicos;

import static br.com.bruno.toshiaki.builders.FilmeBuilder.umFilme;
import static br.com.bruno.toshiaki.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.com.bruno.toshiaki.builders.LocacaoBuilder.umLocacao;
import static br.com.bruno.toshiaki.builders.UsuarioBuilder.umUsuario;
import static br.com.bruno.toshiaki.matchers.MatchersProprios.caiNumaSegunda;
import static br.com.bruno.toshiaki.matchers.MatchersProprios.ehHoje;
import static br.com.bruno.toshiaki.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static br.com.bruno.toshiaki.utils.DataUtils.isMesmaData;
import static br.com.bruno.toshiaki.utils.DataUtils.obterData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import br.com.bruno.toshiaki.daos.LocacaoDao;
import br.com.bruno.toshiaki.entidades.Locacao;
import br.com.bruno.toshiaki.entidades.Usuario;
import br.com.bruno.toshiaki.exceptions.FilmeSemEstoqueException;
import br.com.bruno.toshiaki.exceptions.LocadoraException;
import br.com.bruno.toshiaki.servico.EmailService;
import br.com.bruno.toshiaki.servico.LocacaoService;
import br.com.bruno.toshiaki.servico.SPCService;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class LocacaoServiceTest {


  @Rule
  public ErrorCollector error = new ErrorCollector();
  @Rule
  public ExpectedException exception = ExpectedException.none();

  @InjectMocks
  @Spy
  private LocacaoService service;
  @Mock
  private SPCService spc;
  @Mock
  private LocacaoDao dao;
  @Mock
  private EmailService email;

  public LocacaoServiceTest() {
  }

  @Before
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void deveAlugarFilme() throws Exception {

    //cenario
    final var usuario = umUsuario().agora();
    final var filmes = List.of(umFilme().comValor(5.0).agora());

    doReturn(obterData(28, 4, 2017)).when(this.service).getDataLocacao();
    //acao
    final var locacao = this.service.alugarFilme(usuario, filmes);

    //verificacao
    this.error.checkThat(locacao.getValor(), is(equalTo(5.0)));
    this.error.checkThat(isMesmaData(locacao.getDataLocacao(), obterData(28, 4, 2017)), is(true));
    this.error.checkThat(isMesmaData(locacao.getDataRetorno(), obterData(29, 4, 2017)), is(true));

  }

  @Test(expected = FilmeSemEstoqueException.class)
  public void naoDeveAlugarFilmeSemEstoque() throws Exception {
    //cenario
    final var usuario = umUsuario().agora();
    final var filmes = List.of(umFilmeSemEstoque().agora());

    //acao
    this.service.alugarFilme(usuario, filmes);
  }

  @Test
  public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
    //cenario
    final var filmes = List.of(umFilme().agora());

    //acao
    try {
      this.service.alugarFilme(null, filmes);
      Assert.fail();
    } catch (final LocadoraException e) {
      assertThat(e.getMessage(), is("Usuario vazio"));
    }
  }

  @Test
  public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    final var usuario = umUsuario().agora();

    this.exception.expect(LocadoraException.class);
    this.exception.expectMessage("Filme vazio");

    //acao
    this.service.alugarFilme(usuario, null);
  }


  @Test
  public void deveDevolverSegundaAlugandoSabado() throws FilmeSemEstoqueException, LocadoraException {

    //cenario
    final var usuario = umUsuario().agora();
    final var filmes = List.of(umFilme().agora());
    doReturn(obterData(29, 4, 2017)).when(this.service).getDataLocacao();

    //acao
    final var retorno = this.service.alugarFilme(usuario, filmes);

    //verificacao

    assertThat(retorno.getDataRetorno(), caiNumaSegunda());

  }

  @Test
  public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
    //cenario
    final var usuario = umUsuario().agora();
    final var filmes = Collections.singletonList(umFilme().agora());

    when(this.spc.possuiNegativacao(any(Usuario.class))).thenReturn(true);

    //acao
    try {
      this.service.alugarFilme(usuario, filmes);
      //verificacao
      Assert.fail();
    } catch (final LocadoraException e) {
      Assert.assertThat(e.getMessage(), is("Usuario Negativado"));
    }

    verify(this.spc).possuiNegativacao(usuario);
  }

  @Test
  public void deveEnviarEmailParaLocacoesAtrasadas() {
    //cenario
    final var usuario = umUsuario().agora();

    final var usuario2 = umUsuario().comNome("Usuario em dia").agora();
    final var usuario3 = umUsuario().comNome("Outro atrasado").agora();
    final var locacoes = List.of(
        umLocacao().atrasada().comUsuario(usuario).agora(),
        umLocacao().comUsuario(usuario2).agora(),
        umLocacao().atrasada().comUsuario(usuario3).agora(),
        umLocacao().atrasada().comUsuario(usuario3).agora());
    when(this.dao.obterLocacoesPendentes()).thenReturn(locacoes);

    //acao
    this.service.notificarAtrasos();

    //verificacao
    verify(this.email, times(3)).notificarAtraso(any(Usuario.class));
    verify(this.email).notificarAtraso(usuario);
    verify(this.email, atLeastOnce()).notificarAtraso(usuario3);
    verify(this.email, never()).notificarAtraso(usuario2);
    verifyNoMoreInteractions(this.email);
  }

  @Test
  public void deveTratarErroSPC() throws Exception {
    final var usuario = umUsuario().agora();
    final var filmes = List.of(umFilme().agora());

    when(this.spc.possuiNegativacao(usuario)).thenThrow(new LocadoraException("Problema com SPC, tente novamente"));
    this.exception.expect(LocadoraException.class);
    this.exception.expectMessage("Problema com SPC, tente novamente");

    this.service.alugarFilme(usuario, filmes);
  }

  @Test
  public void deveProrrogarLocacao() {
    final var locao = umLocacao().agora();

    this.service.prorrogarLocacao(locao, 3);
    final var argCap = ArgumentCaptor.forClass(Locacao.class);

    verify(this.dao).salvar(argCap.capture());
    final var locacaoRetorno = argCap.getValue();

    //o errorCheck valida cada instucao enquanto o assertThat so aceita uma validacao por metodo
    this.error.checkThat(locacaoRetorno.getValor(), is(12.0));
    this.error.checkThat(locacaoRetorno.getDataLocacao(), ehHoje());
    this.error.checkThat(locacaoRetorno.getDataRetorno(), ehHojeComDiferencaDias(3));
  }
}
