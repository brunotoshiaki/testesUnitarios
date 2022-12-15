package br.com.bruno.toshiaki.entidades;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class Locacao {

  private Usuario usuario;
  private List<Filme> filmes;
  private Date dataLocacao;
  private Date dataRetorno;
  private Double valor;

}