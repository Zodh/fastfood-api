package br.com.fiap.fastfood.api.adapters.driver.controller.advice;

import br.com.fiap.fastfood.api.core.application.dto.ErrorDetailDTO;
import br.com.fiap.fastfood.api.core.application.dto.ErrorResponseDTO;
import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ErrorResponseDTO> handleDomainException(DomainException domainException) {
    String message = domainException.getMessage();
    List<ErrorDetailDTO> errors = Optional.ofNullable(domainException.getErrors()).orElse(
            new ArrayList<>()).stream()
        .map(errorDetail -> new ErrorDetailDTO(errorDetail.field(), errorDetail.message()))
        .toList();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponseDTO(message, errors));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleNotFoundException(
      NotFoundException notFoundException) {
    String message = notFoundException.getMessage();
    ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(message, new ArrayList<>());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
  }

  @ExceptionHandler(SQLException.class)
  public ResponseEntity<ErrorResponseDTO> handleSqlException(SQLException exception) {
    String message = exception.getMessage();
    ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(message, new ArrayList<>());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
  }

}
