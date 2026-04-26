package br.com.johncobain.livrariaORM.dtos.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationDto (
    @NotBlank(message="Email é obrigatório")
    @Schema(description="Email do usuário", example="john.doe@example.com")
    @Email(message="Email inválido")
    String email,

    @NotBlank(message="Senha é obrigatória")
    @Schema(description="Senha do usuário", example="123456")
    String senha
){
}
