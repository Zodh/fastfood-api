package br.com.fiap.fastfood.api.adapters.gateway;

import br.com.fiap.fastfood.api.infrastructure.dao.repository.invoice.InvoiceRepository;
import br.com.fiap.fastfood.api.application.gateway.InvoiceExpirationPolicyGateway;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InvoiceExpirationPolicyGatewayImpl implements InvoiceExpirationPolicyGateway {

  @Value("${invoice.expiration.time}")
  private int invoiceExpirationTime;

  private final InvoiceRepository invoiceRepository;

  @Autowired
  public InvoiceExpirationPolicyGatewayImpl(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
  @Override
  public void expireOldInvoices() {
    invoiceRepository.expireOldInvoices(invoiceExpirationTime);
  }

}
