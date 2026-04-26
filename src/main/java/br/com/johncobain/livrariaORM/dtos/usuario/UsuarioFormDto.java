package br.com.johncobain.livrariaORM.dtos.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

public record UsuarioFormDto(
    @NotBlank(message="Nome é obrigatório")
    @Schema(description = "Nome do usuário", example = "John Doe")
    String nome,

    @NotBlank(message = "CPF é obrigatório")
    @Schema(description = "CPF do usuário", example = "21499318006")
    @CPF(message = "CPF inválido")
    String cpf,

    @NotBlank(message = "Email é obrigatório")
    @Schema(description = "Email do usuário", example = "john.doe@example.com")
    @Email(message = "Email inválido")
    String email,

    @NotBlank(message = "Senha é obrigatória")
    @Schema(description = "Senha do usuário", example = "123456")
    @Size(min = 6, message = "A senha deve conter no mínimo 6 caracteres")
    @Size(max = 72, message = "A senha deve conter no máximo 72 caracteres")
    String senha,

    @NotBlank(message = "Telefone é obrigatório")
    @Schema(description = "Telefone do usuário", example = "11999999999")
    @Size(min = 10, max = 11, message = "O telefone deve conter entre 10 e 11 caracteres")
    String telefone,
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato 99999-999")
    @Schema(description = "CEP", example = "40000-000")
    String cep,

    @Schema(description = "Número do endereço", example = "123")
    Integer numero,

    @Schema(description = "Complemento do endereço", example = "Apto 123")
    String complemento
) {
}
