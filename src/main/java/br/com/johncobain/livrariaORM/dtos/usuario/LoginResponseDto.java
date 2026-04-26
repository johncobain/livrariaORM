package br.com.johncobain.livrariaORM.dtos.usuario;

public record LoginResponseDto(
    String token,
    String role,
    Long id,
    String nome,
    String email
) {
}
