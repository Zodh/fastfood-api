package br.com.fiap.fastfood.api.application.policy;

import br.com.fiap.fastfood.api.application.dto.followup.FollowUpStateEnum;
import br.com.fiap.fastfood.api.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.application.gateway.mapper.InvoiceMapperApp;
import br.com.fiap.fastfood.api.application.gateway.mapper.OrderMapperApp;
import br.com.fiap.fastfood.api.application.gateway.repository.InvoiceRepositoryGateway;
import br.com.fiap.fastfood.api.application.gateway.repository.OrderRepositoryGateway;
import br.com.fiap.fastfood.api.entities.exception.DomainException;
import br.com.fiap.fastfood.api.entities.exception.ErrorDetail;
import br.com.fiap.fastfood.api.entities.invoice.Invoice;
import br.com.fiap.fastfood.api.entities.invoice.state.InvoiceStateEnum;
import br.com.fiap.fastfood.api.entities.invoice.state.impl.InvoicePaidState;
import br.com.fiap.fastfood.api.entities.invoice.state.impl.InvoicePendingState;
import br.com.fiap.fastfood.api.entities.order.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class OrderInvoicePolicyImpl implements OrderInvoicePolicy {

  private final InvoiceRepositoryGateway invoiceRepositoryGateway;
  private final OrderRepositoryGateway orderRepositoryGateway;
  private final InvoiceMapperApp invoiceMapperApp;
  private final OrderMapperApp orderMapperApp;
  private final FollowUpPolicy followUpPolicy;

  public OrderInvoicePolicyImpl(InvoiceRepositoryGateway invoiceRepositoryGateway, OrderRepositoryGateway orderRepositoryGateway, FollowUpPolicy followUpPolicy, InvoiceMapperApp invoiceMapperApp, OrderMapperApp orderMapperApp) {
    this.invoiceRepositoryGateway = invoiceRepositoryGateway;
    this.orderRepositoryGateway = orderRepositoryGateway;
    this.invoiceMapperApp = invoiceMapperApp;
    this.orderMapperApp = orderMapperApp;
    this.followUpPolicy = followUpPolicy;
  }

  @Override
  public void cancelOrderInvoice(OrderDTO orderDTO) {
    if (Objects.nonNull(orderDTO.getId()) && orderDTO.getId() > 0 && Objects.nonNull(orderDTO.getInvoice()) && orderDTO.getInvoice().getState().equals(
        InvoiceStateEnum.PENDING)) {
      invoiceRepositoryGateway.cancelPendingInvoicesByOrder(orderDTO.getId());
    }
  }

  @Override
  public void generateOrderInvoice(OrderDTO orderDTO) {
    this.cancelOrderInvoice(orderDTO);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));

    Invoice invoice = this.createInvoice(order);

    InvoiceDTO validInvoice = invoiceMapperApp.toDto(invoice);
    orderDTO.setInvoice(invoiceRepositoryGateway.save(validInvoice));
  }

  @Override
  public void defineOrderEligibleToPreparation(OrderDTO orderDTO) {
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));

    order.getState().setAwaitingPreparation();

    OrderDTO orderReadyToPreparation = orderMapperApp.toDTO(order);
    OrderDTO persisted = orderRepositoryGateway.save(orderReadyToPreparation);
    followUpPolicy.updateOrderInFollowUp(persisted,
        FollowUpStateEnum.RECEIVED);
  }

  public Invoice createInvoice(Order order) {
    if (Objects.isNull(order) || Objects.isNull(order.getPrice())) {
      throw new DomainException(new ErrorDetail("order", "Não é possível criar um pagamento para um pedido nulo ou sem um preço definido!"));
    }
    BigDecimal paidAmount = BigDecimal.ZERO;
    if (Objects.nonNull(order.getInvoices()) && !order.getInvoices().isEmpty() && order.getInvoices().stream().anyMatch(i -> i.getState() instanceof InvoicePaidState)) {
      paidAmount = order.getInvoices().stream()
              .filter(i -> Objects.nonNull(i) && Objects.nonNull(i.getState())
                      && i.getState() instanceof InvoicePaidState && Objects.nonNull(i.getPrice()))
              .map(Invoice::getPrice)
              .reduce(BigDecimal.ZERO, BigDecimal::add);
      if (paidAmount.compareTo(order.getPrice()) > 0) {
        throw new DomainException(new ErrorDetail("order.price", "O valor pago é maior do que o valor do pedido! Contate um administrador!"));
      }
    }
    Invoice invoice = new Invoice();
    invoice.changeState(new InvoicePendingState(invoice));
    invoice.getState().setPrice(order.getPrice().subtract(paidAmount));
    invoice.setOrder(order);
    invoice.setCreatedAt(LocalDateTime.now());
    return invoice;
  }

}
