package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.event.PaymentEvent;
import br.com.fiap.fastfood.api.core.application.event.PaymentOperationEnum;
import br.com.fiap.fastfood.api.core.application.exception.ApplicationException;
import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.ports.repository.OrderRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.OrderAggregate;
import br.com.fiap.fastfood.api.core.domain.aggregate.ServiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.CustomerServicePort;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.OrderProductServicePort;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.OrderServicePort;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class OrderServicePortImpl implements OrderServicePort {

  private final OrderRepositoryPort orderRepositoryPort;
  private final CustomerServicePort customerServicePort;
  private final OrderProductServicePort orderProductServicePort;
  private final ApplicationEventPublisher eventPublisher;

  @Autowired
  public OrderServicePortImpl(OrderRepositoryPort orderRepositoryPort, CustomerServicePort customerServicePort, OrderProductServicePort orderProductServicePort, ApplicationEventPublisher eventPublisher) {
    this.orderRepositoryPort = orderRepositoryPort;
    this.customerServicePort = customerServicePort;
    this.orderProductServicePort = orderProductServicePort;
    this.eventPublisher = eventPublisher;
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
  public Order cancel(Long orderId) {
    Order order = getById(orderId);
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.cancelOrder();
    Order cancelledOrder = save(order);
    eventPublisher.publishEvent(new PaymentEvent(this, cancelledOrder, PaymentOperationEnum.CANCEL));
    return cancelledOrder;
  }

  @Override
  public Order confirm(Long orderId) {
    Order order = getById(orderId);
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.confirmOrder();
    Order confirmedOrder = save(order);
    eventPublisher.publishEvent(new PaymentEvent(this, confirmedOrder, PaymentOperationEnum.GENERATE));
    return confirmedOrder;
  }

  private Order save(Order order) {
    Order confirmedOrder = orderRepositoryPort.save(order);
    confirmedOrder.setInvoices(order.getInvoices());
    return confirmedOrder;
  }

  private @Nullable Customer fetchCustomerData(Customer customer) {
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
