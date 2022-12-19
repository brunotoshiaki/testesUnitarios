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


  private Double getValorFilme(int i, Filme filme) {
    return switch (i) {
      case 2 -> filme.getPrecoLocacao() * 0.75;
      case 3 -> filme.getPrecoLocacao() * 0.5;
      case 4 -> filme.getPrecoLocacao() * 0.25;
      case 5 -> 0d;
      default -> filme.getPrecoLocacao();
    };
  }

  private void calculaDataEntrega(Locacao locacao) {
    //Entrega no dia seguinte
    var dataEntrega = new Date();
    dataEntrega = adicionarDias(dataEntrega, 1);

    if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
      dataEntrega = adicionarDias(dataEntrega, 1);
    }

    locacao.setDataRetorno(dataEntrega);
  }

  public void setSpcService(SPCService spcService) {
    this.spcService = spcService;
  }

  private Double getValorTotal(List<Filme> filmes) {
    var valorTotal = 0d;

    for (int i = 0; i < filmes.size(); i++) {
      Filme filme = filmes.get(i);
      var valorFilme = getValorFilme(i, filme);
      valorTotal += valorFilme;
    }
    return valorTotal;
  }

  public Locacao alugarFilme(Usuario usuario, List<Filme> filmes)
      throws FilmeSemEstoqueException, LocadoraException {
    if (usuario == null) {
      throw new LocadoraException("Usuario vazio");
    }

    if (CollectionUtils.isEmpty(filmes)) {
      throw new LocadoraException("Filme vazio");
    }

    for (Filme filme : filmes) {
      if (filme.getEstoque() == 0) {
        throw new FilmeSemEstoqueException();
      }
    }

    if (spcService.possuiNegativacao(usuario)) {
      throw new LocadoraException("Usuario Negativado");
    }

    var locacao = new Locacao();
    locacao.setFilmes(filmes);
    locacao.setUsuario(usuario);
    locacao.setDataLocacao(new Date());
    locacao.setValor(this.getValorTotal(filmes));

    calculaDataEntrega(locacao);

    //Salvando a locacao...
    locacaoDao.salvar(locacao);

    return locacao;
  }

  public void notificarAtrasos() {
  var locacoes = locacaoDao.obterLocacoesPendentes();
    for (Locacao locacao : locacoes) {
      if (locacao.getDataRetorno().before(new Date())) {
        emailService.notificarAtraso(locacao.getUsuario());
      }
    }
  }


  public void setLocacaoDao(LocacaoDao locacaoDao) {
    this.locacaoDao = locacaoDao;
  }


  public void setEmailService(EmailService email) {
    emailService = email;
  }
}