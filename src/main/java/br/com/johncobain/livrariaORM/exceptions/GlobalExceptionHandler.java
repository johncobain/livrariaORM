package br.com.johncobain.livrariaORM.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Map<String,String>> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request){
    Map<String, String> error = new HashMap<>();
    error.put("status", ex.getStatusCode().toString());
    error.put("reason", ex.getReason());
    error.put("path", request.getRequestURI());
    return ResponseEntity.status(ex.getStatusCode()).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String,Object>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request){
    Map<String, Object> response = new HashMap<>();
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    response.put("status", ex.getStatusCode().toString());
    response.put("reason", "Erro de validação");
    response.put("path", request.getRequestURI());
    response.put("errors", errors);

    return ResponseEntity.status(ex.getStatusCode()).body(response);
  }

  @ExceptionHandler(UniqueAttributeAlreadyRegisteredException.class)
  public ResponseEntity<Map<String, String>> handleUniqueAttributeAlreadyRegisteredException(UniqueAttributeAlreadyRegisteredException ex, HttpServletRequest request){
    Map<String, String> error = new HashMap<>();
    error.put("status", ex.getStatusCode().toString());
    error.put("reason", ex.getReason());
    error.put("path", request.getRequestURI());
    return ResponseEntity.status(ex.getStatusCode()).body(error);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, String>> handleGenericRuntimeException(RuntimeException ex, HttpServletRequest request) {
    Map<String, String> error = new HashMap<>();
    HttpStatus status = HttpStatus.BAD_REQUEST;

    error.put("status", String.valueOf(status.value()));
    error.put("reason", ex.getMessage());
    error.put("path", request.getRequestURI());
    return ResponseEntity.status(status).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGenericException(Exception ex, HttpServletRequest request) {
    Map<String, String> error = new HashMap<>();
    HttpStatus status = HttpStatus.BAD_REQUEST;

    error.put("status", String.valueOf(status.value()));
    error.put("reason", ex.getMessage());
    error.put("path", request.getRequestURI());
    return ResponseEntity.status(status).body(error);
  }
}
