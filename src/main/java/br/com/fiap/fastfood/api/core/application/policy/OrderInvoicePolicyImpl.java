package br.com.fiap.fastfood.api.core.application.policy;

import br.com.fiap.fastfood.api.core.application.port.repository.InvoiceRepositoryPort;
import br.com.fiap.fastfood.api.core.application.port.repository.OrderRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.InvoiceAggregate;
import br.com.fiap.fastfood.api.core.domain.aggregate.OrderAggregate;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import java.util.Objects;

public class OrderInvoicePolicyImpl implements OrderInvoicePolicy {

  private final InvoiceRepositoryPort invoiceRepositoryPort;
  private final OrderRepositoryPort orderRepositoryPort;

  public OrderInvoicePolicyImpl(InvoiceRepositoryPort invoiceRepositoryPort, OrderRepositoryPort orderRepositoryPort) {
    this.invoiceRepositoryPort = invoiceRepositoryPort;
    this.orderRepositoryPort = orderRepositoryPort;
  }

  @Override
  public void cancelInvoiceByOrder(Order order) {
    if (Objects.nonNull(order.getId()) && order.getId() > 0 && order.hasInvoice()) {
      invoiceRepositoryPort.cancelPendingInvoicesByOrder(order.getId());
    }
  }

  @Override
  public void generateInvoiceByOrder(Order order) {
    InvoiceAggregate invoiceAggregate = new InvoiceAggregate();
    this.cancelInvoiceByOrder(order);
    Invoice invoice = invoiceAggregate.createInvoice(order);
    invoiceRepositoryPort.save(invoice);
  }

  @Override
  public void payOrderInvoice(Order order) {
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.turnReadyToPrepare();
    orderRepositoryPort.save(order);
    // Politica de follow up.
  }

}
