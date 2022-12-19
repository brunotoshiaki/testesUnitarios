package br.com.bruno.toshiaki.daos;

import br.com.bruno.toshiaki.entidades.Locacao;
import java.util.List;

public interface LocacaoDao {

  void salvar(final Locacao locacao);

  List<Locacao> obterLocacoesPendentes();

}
