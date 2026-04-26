package br.com.johncobain.livrariaORM.services;

import br.com.johncobain.livrariaORM.dtos.usuario.AuthenticationDto;
import br.com.johncobain.livrariaORM.dtos.usuario.LoginResponseDto;
import br.com.johncobain.livrariaORM.dtos.usuario.UsuarioDto;
import br.com.johncobain.livrariaORM.dtos.usuario.UsuarioFormDto;
import br.com.johncobain.livrariaORM.exceptions.UniqueAttributeAlreadyRegisteredException;
import br.com.johncobain.livrariaORM.models.entities.Role;
import br.com.johncobain.livrariaORM.models.entities.Usuario;
import br.com.johncobain.livrariaORM.repositories.RoleRepository;
import br.com.johncobain.livrariaORM.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UsuarioService implements UserDetailsService {
  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private JWTTokenService jwtTokenService;

  @Autowired
  @Lazy
  private AuthenticationManager authenticationManager;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
  }

  public LoginResponseDto login(AuthenticationDto dto){
    var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
    Authentication auth = this.authenticationManager.authenticate(usernamePassword);

    Usuario usuario = (Usuario) auth.getPrincipal();
    var token = this.jwtTokenService.generateToken(usuario);

    String role = usuario.getRoles().stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("User has no roles assigned"))
        .getAuthority();
    
    return new LoginResponseDto(
        token,
        role,
        usuario.getId(),
        usuario.getNome(),
        usuario.getEmail()
    );
  }

  public UsuarioDto register(UsuarioFormDto dto){
    if(usuarioRepository.findByEmail(dto.email()).isPresent()){
      throw new UniqueAttributeAlreadyRegisteredException("email");
    }
    if(usuarioRepository.findByCpf(dto.cpf()).isPresent()){
      throw new UniqueAttributeAlreadyRegisteredException("cpf");
    }

    Role userRole = roleRepository.findByRole("ROLE_USUARIO")
            .orElseThrow(() -> new IllegalStateException("Role 'ROLE_USUARIO' não encontrada."));

    String senha = passwordEncoder.encode(dto.senha());

    Usuario usuario = new Usuario(dto);
    usuario.setSenha(senha);
    usuario.setRoles(new HashSet<>(Set.of(userRole)));

    usuarioRepository.save(usuario);

    return new UsuarioDto(dto.nome(), dto.email());
  }
}
