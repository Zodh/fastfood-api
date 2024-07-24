package br.com.fiap.fastfood.api.core.domain.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorDetail {

  private final String field;
  private final String message;

}
