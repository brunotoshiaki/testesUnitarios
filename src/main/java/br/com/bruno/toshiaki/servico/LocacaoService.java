package br.com.bruno.toshiaki.servico;

import static br.com.bruno.toshiaki.utils.DataUtils.adicionarDias;

import br.com.bruno.toshiaki.daos.LocacaoDao;
import br.com.bruno.toshiaki.entidades.Filme;
import br.com.bruno.toshiaki.entidades.Locacao;
import br.com.bruno.toshiaki.entidades.Usuario;
import br.com.bruno.toshiaki.exceptions.FilmeSemEstoqueException;
import br.com.bruno.toshiaki.exceptions.LocadoraException;
import br.com.bruno.toshiaki.utils.DataUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class LocacaoService {

  private LocacaoDao locacaoDao;
  private SPCService spcService;

  private EmailService emailService;


  private Double getValorFilme(final int i, final Filme filme) {
    return switch (i) {
      case 2 -> filme.getPrecoLocacao() * 0.75;
      case 3 -> filme.getPrecoLocacao() * 0.5;
      case 4 -> filme.getPrecoLocacao() * 0.25;
      case 5 -> 0d;
      default -> filme.getPrecoLocacao();
    };
  }

  private void calculaDataEntrega(final Locacao locacao) {
    //Entrega no dia seguinte
    var dataEntrega = new Date();
    dataEntrega = adicionarDias(dataEntrega, 1);

    if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
      dataEntrega = adicionarDias(dataEntrega, 1);
    }

    locacao.setDataRetorno(dataEntrega);
  }


  private Double getValorTotal(final List<Filme> filmes) {
    var valorTotal = 0d;

    for (int i = 0; i < filmes.size(); i++) {
      final Filme filme = filmes.get(i);
      final var valorFilme = this.getValorFilme(i, filme);
      valorTotal += valorFilme;
    }
    return valorTotal;
  }

  public Locacao alugarFilme(final Usuario usuario, final List<Filme> filmes)
      throws FilmeSemEstoqueException, LocadoraException {
    if (usuario == null) {
      throw new LocadoraException("Usuario vazio");
    }

    if (CollectionUtils.isEmpty(filmes)) {
      throw new LocadoraException("Filme vazio");
    }

    for (final Filme filme : filmes) {
      if (filme.getEstoque() == 0) {
        throw new FilmeSemEstoqueException();
      }
    }
    final boolean negativado;
    try {
      negativado = this.spcService.possuiNegativacao(usuario);
    } catch (final Exception e) {
      throw new LocadoraException("Problema com SPC, tente novamente");
    }
    if (negativado) {
      throw new LocadoraException("Usuario Negativado");
    }

    final var locacao = new Locacao();
    locacao.setFilmes(filmes);
    locacao.setUsuario(usuario);
    locacao.setDataLocacao(new Date());
    locacao.setValor(this.getValorTotal(filmes));

    this.calculaDataEntrega(locacao);

    //Salvando a locacao...
    this.locacaoDao.salvar(locacao);

    return locacao;
  }

  public void notificarAtrasos() {
    final var locacoes = this.locacaoDao.obterLocacoesPendentes();
    for (final Locacao locacao : locacoes) {
      if (locacao.getDataRetorno().before(new Date())) {
        this.emailService.notificarAtraso(locacao.getUsuario());
      }
    }
  }


}