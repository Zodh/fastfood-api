package br.com.fiap.fastfood.api.core.domain.model.order.state.impl;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;

public class OrderOperationNotAllowedException extends DomainException {

  protected static final String PRINCIPAL_SUBJECT = "order";

  public OrderOperationNotAllowedException(
      String subject) {
    super(new ErrorDetail(subject, "A operação não pode ser executada no status atual do pedido!"));
  }

  public OrderOperationNotAllowedException() {
    super(new ErrorDetail(PRINCIPAL_SUBJECT, "A operação não pode ser executada no status atual do pedido!"));
  }
}
