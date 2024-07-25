package br.com.fiap.fastfood.api.core.domain.exception;

import java.util.List;
import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

  private final List<ErrorDetail> errors;

  public DomainException(List<ErrorDetail> errors) {
    super("Existe(m) erro(s) na requisição!");
    this.errors = errors;
  }

  public DomainException(ErrorDetail errorDetail) {
    super("Existe(m) erro(s) na requisição!");
    this.errors = List.of(errorDetail);
  }

}
