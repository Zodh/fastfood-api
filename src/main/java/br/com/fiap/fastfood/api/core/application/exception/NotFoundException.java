package br.com.fiap.fastfood.api.core.application.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }

}
