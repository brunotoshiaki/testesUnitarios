package br.com.bruno.toshiaki.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Filme {

  private String nome;
  private Integer estoque;
  private Double precoLocacao;


}