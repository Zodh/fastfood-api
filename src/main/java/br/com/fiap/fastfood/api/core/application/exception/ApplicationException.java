package br.com.fiap.fastfood.api.core.application.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

  private String reason;

  public ApplicationException(String message, String reason) {
    super(message);
    this.reason = reason;
  }
}
