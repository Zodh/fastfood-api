package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InvoiceStatus {
  EXPIRED,
  PENDING,
  PAID,
  CANCELLED;

}
