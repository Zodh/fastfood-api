package br.com.fiap.fastfood.api.core.domain.model.invoice.state;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InvoiceStateEnum {

  EXPIRED,
  PENDING,
  PAID,
  CANCELLED;

}
