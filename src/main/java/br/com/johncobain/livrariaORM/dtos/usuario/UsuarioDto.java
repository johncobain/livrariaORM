package br.com.johncobain.livrariaORM.dtos.usuario;

import br.com.johncobain.livrariaORM.models.entities.Usuario;

public record UsuarioDto(
    String nome,
    String email
) {
  public static UsuarioDto fromEntity(Usuario usuario) {
    return new UsuarioDto(usuario.getNome(), usuario.getEmail());
  }
}
