package br.com.fiap.fastfood.api.entities.invoice.state;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InvoiceStateEnum {

  EXPIRED,
  PENDING,
  PAID,
  CANCELLED;

}
