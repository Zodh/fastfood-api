package br.com.fiap.fastfood.api.core.application.port.outbound;

public interface InvoiceExpirationPolicyPort {

  void expireOldInvoices();

}
