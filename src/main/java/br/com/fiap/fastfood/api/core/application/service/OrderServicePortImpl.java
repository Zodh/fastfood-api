package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.port.inbound.policy.OrderInvoicePolicyPort;
import br.com.fiap.fastfood.api.core.application.exception.ApplicationException;
import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.port.repository.OrderRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.OrderAggregate;
import br.com.fiap.fastfood.api.core.domain.aggregate.ServiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.CustomerServicePort;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.OrderProductServicePort;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.OrderServicePort;
import java.util.Objects;

public class OrderServicePortImpl implements OrderServicePort {

  private final OrderRepositoryPort orderRepositoryPort;
  private final CustomerServicePort customerServicePort;
  private final OrderProductServicePort orderProductServicePort;
  private final OrderInvoicePolicyPort orderInvoicePolicyPort;

  public OrderServicePortImpl(OrderRepositoryPort orderRepositoryPort, CustomerServicePort customerServicePort, OrderProductServicePort orderProductServicePort, OrderInvoicePolicyPort orderInvoicePolicyPort) {
    this.orderRepositoryPort = orderRepositoryPort;
    this.customerServicePort = customerServicePort;
    this.orderProductServicePort = orderProductServicePort;
    this.orderInvoicePolicyPort = orderInvoicePolicyPort;
  }

  @Override
  public Order create(Customer customer, Collaborator collaborator) {
    customer = fetchCustomerData(customer);
    ServiceAggregate serviceAggregate = new ServiceAggregate(customer);
    Order order = serviceAggregate.createOrder(collaborator);
    return orderRepositoryPort.save(order);
  }

  @Override
  public Order includeOrderProduct(Long orderId, OrderProduct orderProduct) {
    Order order = getById(orderId);
    OrderProduct detailed = orderProductServicePort.create(order, orderProduct);
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.includeOrderProduct(detailed);
    return orderRepositoryPort.save(order);
  }

  @Override
  public Order getById(Long orderId) {
    return orderRepositoryPort.findById(orderId)
        .orElseThrow(() -> new NotFoundException("Não foi encontrado nenhum pedido com o identificador informado!"));
  }

  @Override
  public Order removeOrderProduct(Long orderId, Long orderProductId) {
    Order order = getById(orderId);
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.removeOrderProduct(orderProductId);
    orderProductServicePort.delete(orderProductId);
    return orderRepositoryPort.save(order);
  }

  @Override
  public void cancel(Long orderId) {
    Order order = getById(orderId);
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.cancelOrder();
    Order cancelledOrder = save(order);
    orderInvoicePolicyPort.cancelInvoiceByOrder(cancelledOrder);
  }

  @Override
  public Order confirm(Long orderId) {
    Order order = getById(orderId);
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.confirmOrder();
    Order confirmedOrder = save(order);
    orderInvoicePolicyPort.generateInvoiceByOrder(confirmedOrder);
    return confirmedOrder;
  }

  @Override
  public void turnReadyToPrepare(Long orderId) {
    Order order = getById(orderId);
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.turnReadyToPrepare();
    save(order);
  }

  @Override
  public void prepare(Long orderId) {
    Order order = getById(orderId);
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.initializePreparation();
    orderRepositoryPort.save(order);
    // Politica de follow up.
  }

  @Override
  public void turnReadyToPick(Long orderId) {
    Order order = getById(orderId);
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.setReadyToCollection();
    orderRepositoryPort.save(order);
    // Politica de follow up.
  }

  @Override
  public void finish(Long orderId) {
    Order order = getById(orderId);
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.finishOrder();
    orderRepositoryPort.save(order);
    // Politica de follow up.
  }

  private Order save(Order order) {
    Order confirmedOrder = orderRepositoryPort.save(order);
    confirmedOrder.setInvoices(order.getInvoices());
    return confirmedOrder;
  }

  private Customer fetchCustomerData(Customer customer) {
    if (Objects.nonNull(customer)) {
      if (Objects.isNull(customer.getId()) && Objects.isNull(customer.getDocument())) {
        throw new ApplicationException("O identificador ou o documento do cliente precisa ser informado para constar no pedido!", "Cliente sem identificadores válidos!");
      }
      if (Objects.nonNull(customer.getId())) {
        customer = customerServicePort.getById(customer.getId());
      }
      if (Objects.nonNull(customer.getDocument())) {
        customer = customerServicePort.getByDocument(customer.getDocument());
      }
    }
    return customer;
  }


}
