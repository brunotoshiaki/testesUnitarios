package br.com.bruno.toshiaki.servico;

import static br.com.bruno.toshiaki.utils.DataUtils.adicionarDias;

import br.com.bruno.toshiaki.entidades.Filme;
import br.com.bruno.toshiaki.entidades.Locacao;
import br.com.bruno.toshiaki.entidades.Usuario;
import br.com.bruno.toshiaki.exceptions.FilmeSemEstoqueException;
import br.com.bruno.toshiaki.exceptions.LocadoraException;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class LocacaoService {

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

    var locacao = new Locacao();
    locacao.setFilmes(filmes);
    locacao.setUsuario(usuario);
    locacao.setDataLocacao(new Date());
    var valorTotal = 0d;
    for (Filme filme : filmes) {
      valorTotal += filme.getPrecoLocacao();
    }
    locacao.setValor(valorTotal);

    //Entrega no dia seguinte
    var dataEntrega = new Date();
    dataEntrega = adicionarDias(dataEntrega, 1);
    locacao.setDataRetorno(dataEntrega);

    //Salvando a locacao...
    //TODO adicionar método para salvar

    return locacao;
  }


}