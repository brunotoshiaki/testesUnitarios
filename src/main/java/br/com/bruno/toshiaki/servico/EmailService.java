package br.com.bruno.toshiaki.servico;


import br.com.bruno.toshiaki.entidades.Usuario;

public interface EmailService {

  void notificarAtraso(Usuario usuario);

}
