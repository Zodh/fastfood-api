package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.dto.collaborator.CollaboratorDTO;
import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.core.application.mapper.CollaboratorMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.CollaboratorMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.mapper.CustomerMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.CustomerMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.mapper.InvoiceMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.InvoiceMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.mapper.OrderMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.OrderMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.mapper.OrderProductMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.OrderProductMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.policy.OrderInvoicePolicy;
import br.com.fiap.fastfood.api.core.application.exception.ApplicationException;
import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.port.repository.OrderRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.EstablishmentAggregate;
import br.com.fiap.fastfood.api.core.domain.aggregate.OrderAggregate;
import br.com.fiap.fastfood.api.core.domain.aggregate.ServiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.CustomerServicePort;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.OrderProductServicePort;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.OrderServicePort;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class OrderServicePortImpl implements OrderServicePort {

  private final OrderRepositoryPort orderRepositoryPort;
  private final CustomerServicePort customerServicePort;
  private final OrderProductServicePort orderProductServicePort;
  private final OrderInvoicePolicy orderInvoicePolicy;
  private final OrderMapperApp orderMapperApp;
  private final OrderProductMapperApp orderProductMapperApp;
  private final CustomerMapperApp customerMapperApp;
  private final CollaboratorMapperApp collaboratorMapperApp;
  private final InvoiceMapperApp invoiceMapperApp;

  public OrderServicePortImpl(OrderRepositoryPort orderRepositoryPort, CustomerServicePort customerServicePort, OrderProductServicePort orderProductServicePort, OrderInvoicePolicy orderInvoicePolicy) {
    this.orderRepositoryPort = orderRepositoryPort;
    this.customerServicePort = customerServicePort;
    this.orderProductServicePort = orderProductServicePort;
    this.orderInvoicePolicy = orderInvoicePolicy;
    this.orderMapperApp = new OrderMapperAppImpl();
    this.orderProductMapperApp = new OrderProductMapperAppImpl();
    this.customerMapperApp = new CustomerMapperAppImpl();
    this.collaboratorMapperApp = new CollaboratorMapperAppImpl();
    this.invoiceMapperApp = new InvoiceMapperAppImpl();
  }

  @Override
  public OrderDTO create(CustomerDTO customerDTO, CollaboratorDTO collaboratorDTO) {
    customerDTO = fetchCustomerData(customerDTO);
    Customer customer = customerMapperApp.toDomain(customerDTO);
    Collaborator collaborator = collaboratorMapperApp.toDomain(collaboratorDTO);
    ServiceAggregate serviceAggregate = new ServiceAggregate(customer);
    Order order = serviceAggregate.createOrder(collaborator);
    OrderDTO validOrder = orderMapperApp.toDTO(order);
    return orderRepositoryPort.save(validOrder);
  }

  @Override
  public OrderDTO includeOrderProduct(Long orderId, OrderProductDTO orderProductDTO) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.setState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));
    OrderProductDTO detailedDTO = orderProductServicePort.create(orderDTO, orderProductDTO);
    OrderProduct detailed = orderProductMapperApp.toDomain(detailedDTO);
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.includeOrderProduct(detailed);
    OrderDTO detailedOrderDTO = orderMapperApp.toDTO(order);
    return orderRepositoryPort.save(detailedOrderDTO);
  }

  @Override
  public OrderDTO getById(Long orderId) {
    OrderDTO orderDTO = orderRepositoryPort.findById(orderId).orElseThrow(() -> new NotFoundException("Não foi encontrado nenhum pedido com o identificador informado!"));
    Order order = orderMapperApp.toDomain(orderDTO);
    Invoice activeInvoice = order.getActiveInvoiceOrNull();
    InvoiceDTO activeInvoiceDTO = invoiceMapperApp.toDto(activeInvoice);
    orderDTO.setInvoice(activeInvoiceDTO);
    return orderDTO;
  }

  @Override
  public OrderDTO removeOrderProduct(Long orderId, Long orderProductId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.setState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.removeOrderProduct(orderProductId);
    orderProductServicePort.delete(orderProductId);
    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    return orderRepositoryPort.save(updatedOrder);
  }

  @Override
  public void cancel(Long orderId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.setState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.cancelOrder();
    OrderDTO updatedOrderDTO = orderMapperApp.toDTO(order);
    OrderDTO cancelledOrder = save(updatedOrderDTO);
    orderInvoicePolicy.cancelOrderInvoice(cancelledOrder);
  }

  @Override
  public OrderDTO confirm(Long orderId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.setState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));
    OrderAggregate aggregate = new OrderAggregate(order);
    aggregate.confirmOrder();
    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    OrderDTO confirmedOrder = save(updatedOrder);
    orderInvoicePolicy.generateOrderInvoice(confirmedOrder);
    return confirmedOrder;
  }

  @Override
  public void turnReadyToPrepare(Long orderId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.setState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));
    EstablishmentAggregate aggregate = new EstablishmentAggregate(order);
    aggregate.turnReadyToPrepare();
    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    save(updatedOrder);
  }

  @Override
  public void prepare(Long orderId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.setState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));
    EstablishmentAggregate aggregate = new EstablishmentAggregate(order);
    aggregate.initializePreparation();
    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    orderRepositoryPort.save(updatedOrder);
    // Politica de follow up.
  }

  @Override
  public void turnReadyToPick(Long orderId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.setState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));
    EstablishmentAggregate aggregate = new EstablishmentAggregate(order);
    aggregate.setReadyToCollection();
    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    orderRepositoryPort.save(updatedOrder);
    // Politica de follow up.
    // Send notification.
  }

  @Override
  public void finish(Long orderId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.setState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));
    EstablishmentAggregate aggregate = new EstablishmentAggregate(order);
    aggregate.finishOrder();
    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    orderRepositoryPort.save(updatedOrder);
    // Politica de follow up.
  }

  private OrderDTO save(OrderDTO orderDTO) {
    OrderDTO confirmedOrder = orderRepositoryPort.save(orderDTO);
    confirmedOrder.setInvoices(orderDTO.getInvoices());
    return confirmedOrder;
  }

  private CustomerDTO fetchCustomerData(CustomerDTO customer) {
    if (Objects.nonNull(customer)) {
      if (Objects.isNull(customer.getId()) && (StringUtils.isBlank(customer.getDocumentNumber()) || Objects.isNull(customer.getDocumentType()))) {
        throw new ApplicationException("O identificador ou o documento do cliente precisa ser informado para constar no pedido!", "Cliente sem identificadores válidos!");
      }
      if (Objects.nonNull(customer.getId())) {
        customer = customerServicePort.getById(customer.getId());
      }
      if (StringUtils.isNotBlank(customer.getDocumentNumber()) && Objects.nonNull(customer.getDocumentType())) {
        customer = customerServicePort.getByDocument(customer.getDocumentNumber(), customer.getDocumentType());
      }
    }
    return customer;
  }


}
