package br.com.fiap.fastfood.api.core.domain.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }
}
