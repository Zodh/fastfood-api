package br.com.fiap.fastfood.api.core.application.policy;

import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpStateEnum;
import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.mapper.InvoiceMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.InvoiceMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.mapper.OrderMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.OrderMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.port.repository.InvoiceRepositoryPort;
import br.com.fiap.fastfood.api.core.application.port.repository.OrderRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.EstablishmentAggregate;
import br.com.fiap.fastfood.api.core.domain.aggregate.InvoiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceStateEnum;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import java.util.Objects;

public class OrderInvoicePolicyImpl implements OrderInvoicePolicy {

  private final InvoiceRepositoryPort invoiceRepositoryPort;
  private final OrderRepositoryPort orderRepositoryPort;
  private final InvoiceMapperApp invoiceMapperApp;
  private final OrderMapperApp orderMapperApp;
  private final FollowUpPolicy followUpPolicy;

  public OrderInvoicePolicyImpl(InvoiceRepositoryPort invoiceRepositoryPort, OrderRepositoryPort orderRepositoryPort, FollowUpPolicy followUpPolicy) {
    this.invoiceRepositoryPort = invoiceRepositoryPort;
    this.orderRepositoryPort = orderRepositoryPort;
    this.invoiceMapperApp = new InvoiceMapperAppImpl();
    this.orderMapperApp = new OrderMapperAppImpl();
    this.followUpPolicy = followUpPolicy;
  }

  @Override
  public void cancelOrderInvoice(OrderDTO orderDTO) {
    if (Objects.nonNull(orderDTO.getId()) && orderDTO.getId() > 0 && Objects.nonNull(orderDTO.getInvoice()) && orderDTO.getInvoice().getState().equals(
        InvoiceStateEnum.PENDING)) {
      invoiceRepositoryPort.cancelPendingInvoicesByOrder(orderDTO.getId());
    }
  }

  @Override
  public void generateOrderInvoice(OrderDTO orderDTO) {
    InvoiceAggregate invoiceAggregate = new InvoiceAggregate();
    this.cancelOrderInvoice(orderDTO);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));
    Invoice invoice = invoiceAggregate.createInvoice(order);
    InvoiceDTO validInvoice = invoiceMapperApp.toDto(invoice);
    orderDTO.setInvoice(invoiceRepositoryPort.save(validInvoice));
  }

  @Override
  public void defineOrderEligibleToPreparation(OrderDTO orderDTO) {
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));
    EstablishmentAggregate aggregate = new EstablishmentAggregate(order);
    aggregate.turnReadyToPrepare();
    OrderDTO orderReadyToPreparation = orderMapperApp.toDTO(order);
    OrderDTO persisted = orderRepositoryPort.save(orderReadyToPreparation);
    followUpPolicy.updateOrderInFollowUp(persisted,
        FollowUpStateEnum.RECEIVED);
  }

}
