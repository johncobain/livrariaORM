package br.com.johncobain.livrariaORM.models.entities;

import br.com.johncobain.livrariaORM.dtos.usuario.UsuarioFormDto;
import br.com.johncobain.livrariaORM.models.enums.Status;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nome;

  @Column(nullable = false, unique = true, length = 14)
  private String cpf;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, length = 72)
  private String senha;

  @Column(nullable = false, length = 18)
  private String telefone;

  @Column(nullable = false, length = 9)
  private String cep;
  private Integer numero;
  private String complemento;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private Status status = Status.ATIVO;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "usuarios_roles",
      joinColumns = @JoinColumn(name = "usuario_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles = new HashSet<>();

  @Column(name = "created_at", updatable = false)
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @PrePersist
  protected void onCreate() {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  protected void onUpdate() {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    updatedAt = now;
  }

  public Usuario(){}

  public Usuario(String nome, String cpf, String email, String senha, String telefone, String cep, Integer numero, String complemento, Set<Role> roles) {
    this.nome = nome;
    this.cpf = cpf;
    this.email = email;
    this.senha = senha;
    this.telefone = telefone;
    this.cep = cep;
    this.numero = numero;
    this.complemento = complemento;
    this.roles = roles;
  }

  public Usuario(UsuarioFormDto dto){
    this.nome = dto.nome();
    this.cpf = dto.cpf();
    this.email = dto.email();
    this.senha = dto.senha();
    this.telefone = dto.telefone();
    this.cep = dto.cep();
    this.numero = dto.numero();
    this.complemento = dto.complemento();
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }

  public String getCpf() { return cpf; }
  public void setCpf(String cpf) { this.cpf = cpf; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getSenha() { return senha; }
  public void setSenha(String senha) { this.senha = senha; }

  public String getTelefone() { return telefone; }
  public void setTelefone(String telefone) { this.telefone = telefone; }

  public String getCep() { return cep; }
  public void setCep(String cep) { this.cep = cep; }

  public Integer getNumero() { return numero; }
  public void setNumero(Integer numero) { this.numero = numero; }

  public String getComplemento() { return complemento; }
  public void setComplemento(String complemento) { this.complemento = complemento; }

  public Status getStatus() { return status; }
  public void setStatus(Status status) { this.status = status; }

  public Set<Role> getRoles() { return roles; }
  public void setRoles(Set<Role> roles) { this.roles = roles; }
  public void setRole(Role role){ this.roles.add(role); }

  public Timestamp getCreatedAt() { return createdAt; }
  public Timestamp getUpdatedAt() { return updatedAt; }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return this.getSenha();
  }

  @Override
  public String getUsername() {
    return this.getEmail();
  }
}
