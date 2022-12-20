package br.com.bruno.toshiaki.servico;

import br.com.bruno.toshiaki.entidades.Usuario;
import br.com.bruno.toshiaki.exceptions.LocadoraException;

public interface SPCService {

  boolean possuiNegativacao(final Usuario usuario) throws LocadoraException;

}
