package br.com.johncobain.livrariaORM.repositories;

import br.com.johncobain.livrariaORM.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  Optional<Usuario> findByEmail(String email);
  Optional<Usuario> findByCpf(String cpf);
}
