package br.com.johncobain.livrariaORM.services;

import br.com.johncobain.livrariaORM.dtos.usuario.UsuarioFormDto;
import br.com.johncobain.livrariaORM.models.entities.Role;
import br.com.johncobain.livrariaORM.models.entities.Usuario;
import br.com.johncobain.livrariaORM.repositories.RoleRepository;
import br.com.johncobain.livrariaORM.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class DataInitializationService implements CommandLineRunner {
  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void run(String ... args) throws Exception {
    Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
    Role userRole = createRoleIfNotFound("ROLE_USUARIO");

    createUserIfNotFound("Admin", "00000000000", "admin@livraria.com", "admin123", "99999999999", "40000-000", "123", "casa", Set.of(adminRole));
  }

  private Role createRoleIfNotFound(String roleName) {
    return roleRepository.findByRole(roleName)
        .orElseGet(() -> roleRepository.save(new Role(roleName)));
  }

  private void createUserIfNotFound(String nome, String cpf, String email, String senha, String telefone, String cep, String numero, String complemento, Set<Role> roles){
    if(usuarioRepository.findByEmail(email).isEmpty()){
      Usuario usuario = new Usuario();
      usuario.setNome(nome);
      usuario.setCpf(cpf);
      usuario.setEmail(email);
      usuario.setSenha(passwordEncoder.encode(senha));
      usuario.setTelefone(telefone);
      usuario.setCep(cep);
      usuario.setNumero(Integer.parseInt(numero));
      usuario.setComplemento(complemento);
      usuario.setRoles(roles);

      usuarioRepository.save(usuario);
    }
  }
}
