package br.com.fiap.fastfood.api.adapters.driver.controller.advice;

import br.com.fiap.fastfood.api.adapters.driver.dto.ErrorDetailDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.ErrorResponseDTO;
import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.NotFoundException;
import java.util.Collections;
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
            Collections.emptyList()).stream()
        .map(errorDetail -> new ErrorDetailDTO(errorDetail.getField(), errorDetail.getMessage()))
        .toList();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO(message, errors));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException notFoundException) {
    String message = notFoundException.getMessage();
    ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(message, Collections.emptyList());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
  }

}
