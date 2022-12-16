package br.com.bruno.toshiaki.builders;

import br.com.bruno.toshiaki.entidades.Usuario;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsuarioBuilder {

  private Usuario usuario;


  private UsuarioBuilder() {
  }

  public static UsuarioBuilder umUsuario() {
    var build = new UsuarioBuilder();
    build.usuario = new Usuario();
    build.usuario.setNome("Usuario 1");
    return build;
  }

  public Usuario agora() {
    return usuario;
  }
}
