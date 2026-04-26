package br.com.johncobain.livrariaORM.controllers;

import br.com.johncobain.livrariaORM.dtos.usuario.AuthenticationDto;
import br.com.johncobain.livrariaORM.dtos.usuario.LoginResponseDto;
import br.com.johncobain.livrariaORM.dtos.usuario.UsuarioDto;
import br.com.johncobain.livrariaORM.dtos.usuario.UsuarioFormDto;
import br.com.johncobain.livrariaORM.models.entities.Usuario;
import br.com.johncobain.livrariaORM.services.UsuarioService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsuarioController {
  @Autowired
  private UsuarioService usuarioService;

  @GetMapping("/profile")
  public ResponseEntity<UsuarioDto> getCurrentUsuario(@AuthenticationPrincipal Usuario usuario){
    return ResponseEntity.ok(UsuarioDto.fromEntity(usuario));
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody AuthenticationDto dto){
    LoginResponseDto response = usuarioService.login(dto);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/register")
  @ApiResponse(responseCode = "201")
  public ResponseEntity<UsuarioDto> register(@Valid @RequestBody UsuarioFormDto dto){
    UsuarioDto usuario = usuarioService.register(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
  }
}
