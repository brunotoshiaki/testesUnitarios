package br.com.bruno.toshiaki.entidades;

import java.util.Date;
import lombok.Data;

@Data
public class Locacao {

  private Usuario usuario;
  private Filme filme;
  private Date dataLocacao;
  private Date dataRetorno;
  private Double valor;

}