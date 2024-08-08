package br.com.fiap.fastfood.api.adapters.driven.infrastructure.scheduler;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.invoice.InvoiceRepository;
import br.com.fiap.fastfood.api.core.application.port.outbound.InvoiceExpirationPolicyPort;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InvoiceExpirationPolicyPortImpl implements InvoiceExpirationPolicyPort {

  @Value("${invoice.expiration.time}")
  private int invoiceExpirationTime;

  private final InvoiceRepository invoiceRepository;

  @Autowired
  public InvoiceExpirationPolicyPortImpl(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
  @Override
  public void expireOldInvoices() {
    invoiceRepository.expireOldInvoices(invoiceExpirationTime);
  }

}
