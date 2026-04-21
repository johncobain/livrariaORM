package br.com.johncobain.livrariaORM.services;

import br.com.johncobain.livrariaORM.models.entities.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class JWTTokenService {
  @Value("${api.security.token.secret}")
  private String secret;

  private String generateToken(Usuario usuario){
    try{
      var algorithm = Algorithm.HMAC256(secret);
      return JWT.create()
          .withIssuer("livraria")
          .withSubject(usuario.getEmail())
          .withExpiresAt(expirationDate())
          .sign(algorithm);
    } catch (JWTCreationException e){
      throw new RuntimeException("Erro ao gerar token");
    }
  }

  public String getSubject(String tokenJWT){
    try{
      var algorithm = Algorithm.HMAC256(secret);
      return JWT.require(algorithm)
          .withIssuer("livraria")
          .build()
          .verify(tokenJWT)
          .getSubject();
    } catch (JWTVerificationException e) {
      throw new RuntimeException("Token inválido");
    }
  }

  private Instant expirationDate() {
    return LocalDateTime.now().plusHours(22).toInstant(ZoneOffset.of("-03:00"));
  }
}
