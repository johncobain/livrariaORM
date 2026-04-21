package br.com.johncobain.livrariaORM.config.security;

import br.com.johncobain.livrariaORM.repositories.UsuarioRepository;
import br.com.johncobain.livrariaORM.services.JWTTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
  @Autowired
  private JWTTokenService tokenService;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String token = getToken(request);

    if(token != null){
      String email = tokenService.getSubject(token);

      if(email != null){
        UserDetails user = usuarioRepository.findByEmail(email)
            .orElse(null);

        if(user != null){
          var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    }
    filterChain.doFilter(request, response);
  }

  private String getToken(HttpServletRequest request){
    String token = request.getHeader("Authorization");
    if(token == null || !token.startsWith("Bearer ")){
      return null;
    }
    return token.replace("Bearer ", "");
  }
}
