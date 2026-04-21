package br.com.johncobain.livrariaORM.repositories;

import br.com.johncobain.livrariaORM.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByRole(String role);
  boolean existsByRole(String role);
}
