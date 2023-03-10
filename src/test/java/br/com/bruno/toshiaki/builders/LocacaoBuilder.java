package br.com.bruno.toshiaki.builders;


import static br.com.bruno.toshiaki.builders.FilmeBuilder.umFilme;
import static br.com.bruno.toshiaki.builders.UsuarioBuilder.umUsuario;
import static br.com.bruno.toshiaki.utils.DataUtils.obterDataComDiferencaDias;

import br.com.bruno.toshiaki.entidades.Locacao;
import br.com.bruno.toshiaki.entidades.Usuario;
import java.util.Collections;
import java.util.Date;

public class LocacaoBuilder {

  private Locacao elemento;

  private LocacaoBuilder() {
  }

  public static LocacaoBuilder umLocacao() {
    LocacaoBuilder builder = new LocacaoBuilder();
    inicializarDadosPadroes(builder);
    return builder;
  }

  public static void inicializarDadosPadroes(LocacaoBuilder builder) {
    builder.elemento = new Locacao();
    Locacao elemento = builder.elemento;

    elemento.setUsuario(umUsuario().agora());
    elemento.setFilmes(Collections.singletonList(umFilme().agora()));
    elemento.setDataLocacao(new Date());
    elemento.setDataRetorno(obterDataComDiferencaDias(1));
    elemento.setValor(4.0);
  }

  public LocacaoBuilder comUsuario(Usuario param) {
    elemento.setUsuario(param);
    return this;
  }


  public Locacao agora() {
    return elemento;
  }

  public LocacaoBuilder atrasada() {
    elemento.setDataLocacao(obterDataComDiferencaDias(-4));
    elemento.setDataRetorno(obterDataComDiferencaDias(-2));
    return this;
  }
}
