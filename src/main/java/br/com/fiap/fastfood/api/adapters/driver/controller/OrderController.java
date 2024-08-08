package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.email.ActivationCodeLinkGenerator;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.email.EmailSender;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CollaboratorMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CollaboratorMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CustomerMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CustomerMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.InvoiceMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.InvoiceMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderProductMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderProductMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.CustomerRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.InvoiceRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.MenuProductRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.OrderProductRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.OrderRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.person.activation.ActivationCodeRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.driver.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.order.CreateOrderRequestDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.order.PaidOrderResponseDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.core.application.policy.OrderInvoicePolicyImpl;
import br.com.fiap.fastfood.api.core.application.service.CustomerServicePortImpl;
import br.com.fiap.fastfood.api.core.application.service.InvoiceServicePortImpl;
import br.com.fiap.fastfood.api.core.application.service.MenuProductServicePortImpl;
import br.com.fiap.fastfood.api.core.application.service.OrderProductServicePortImpl;
import br.com.fiap.fastfood.api.core.application.service.OrderServicePortImpl;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.InvoiceServicePort;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.OrderServicePort;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderServicePort orderServicePort;
  private final OrderMapper orderMapper;
  private final InvoiceServicePort invoiceServicePort;
  private final InvoiceMapper invoiceMapper;

  private final CustomerMapper customerMapper;
  private final CollaboratorMapper collaboratorMapper;
  private final OrderProductMapper orderProductMapper;

  @Autowired
  public OrderController(
      OrderRepositoryAdapterImpl orderRepositoryAdapter,
      CustomerRepositoryAdapterImpl customerRepositoryAdapter,
      EmailSender emailSenderAdapter,
      ActivationCodeRepositoryAdapterImpl activationCodeRepositoryAdapter,
      ActivationCodeLinkGenerator activationCodeLinkGenerator,
      OrderProductRepositoryAdapterImpl orderProductRepositoryAdapter,
      MenuProductRepositoryAdapterImpl menuProductRepositoryAdapter,
      InvoiceRepositoryAdapterImpl invoiceRepositoryAdapter
  ) {
    CustomerServicePortImpl customerServicePortImpl = new CustomerServicePortImpl(
        customerRepositoryAdapter, emailSenderAdapter, activationCodeRepositoryAdapter,
        activationCodeLinkGenerator);
    MenuProductServicePortImpl menuProductServicePortImpl = new MenuProductServicePortImpl(
        menuProductRepositoryAdapter);
    OrderProductServicePortImpl orderProductServicePort = new OrderProductServicePortImpl(
        orderProductRepositoryAdapter, menuProductServicePortImpl);
    OrderInvoicePolicyImpl orderInvoicePolicyPort = new OrderInvoicePolicyImpl(
        invoiceRepositoryAdapter, orderRepositoryAdapter);
    this.orderServicePort = new OrderServicePortImpl(orderRepositoryAdapter,
        customerServicePortImpl, orderProductServicePort, orderInvoicePolicyPort);
    this.invoiceServicePort = new InvoiceServicePortImpl(invoiceRepositoryAdapter,
        orderInvoicePolicyPort);
    this.customerMapper = new CustomerMapperImpl();
    this.collaboratorMapper = new CollaboratorMapperImpl();
    this.orderMapper = new OrderMapperImpl();
    this.orderProductMapper = new OrderProductMapperImpl();
    this.invoiceMapper = new InvoiceMapperImpl();
  }

  @PostMapping
  public ResponseEntity<OrderDTO> create(@RequestBody(required = false) CreateOrderRequestDTO request) {
    Customer customer = null;
    Collaborator collaborator = null;
    if (Objects.nonNull(request)) {
      customer = customerMapper.toDomain(request.getCustomer());
      collaborator = collaboratorMapper.toDomain(request.getCollaborator());
    }
    Order order = orderServicePort.create(customer, collaborator);
    OrderDTO orderDTO = orderMapper.toDto(order);
    return ResponseEntity.status(HttpStatus.OK).body(orderDTO);
  }

  @PostMapping("/{id}/products")
  public ResponseEntity<OrderDTO> includeOrderProduct(@PathVariable Long id, @RequestBody
      OrderProductDTO orderProductDTO) {
    OrderProduct orderProduct = orderProductMapper.toDomain(orderProductDTO);
    Order order = orderServicePort.includeOrderProduct(id, orderProduct);
    OrderDTO result = orderMapper.toDto(order);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @DeleteMapping("/{id}/products/{orderProductId}")
  public ResponseEntity<OrderDTO> removeOrderProduct(@PathVariable Long id, @PathVariable Long orderProductId) {
    Order order = orderServicePort.removeOrderProduct(id, orderProductId);
    OrderDTO result = orderMapper.toDto(order);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @PatchMapping("/{id}/confirm")
  public ResponseEntity<OrderDTO> confirmOrder(@PathVariable Long id) {
    Order confirmedOrder = orderServicePort.confirm(id);
    OrderDTO result = orderMapper.toDto(confirmedOrder);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @PatchMapping("/{id}/pay")
  public ResponseEntity<PaidOrderResponseDTO> executeFakeCheckout(@PathVariable Long id) {
    Order order = orderServicePort.getById(id);
    invoiceServicePort.executeFakeCheckout(order);
    PaidOrderResponseDTO result = PaidOrderResponseDTO.builder().orderNumber(order.getId()).build();
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @GetMapping("/{id}/invoice")
  public ResponseEntity<InvoiceDTO> getOrderInvoice(@PathVariable Long id) {
    Invoice orderInvoice = orderServicePort.getById(id).getInvoice();
    InvoiceDTO result = invoiceMapper.toDto(orderInvoice);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @DeleteMapping("/{id}/cancel")
  public ResponseEntity<Void> cancel(@PathVariable Long id) {
    orderServicePort.cancel(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/prepare")
  public ResponseEntity<Void> startOrderPreparation(@PathVariable Long id) {
    orderServicePort.prepare(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/ready-to-pick")
  public ResponseEntity<Void> turnOrderReadyToPick(@PathVariable Long id) {
    orderServicePort.turnReadyToPick(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/finish")
  public ResponseEntity<Void> finishOrder(@PathVariable Long id) {
    orderServicePort.finish(id);
    return ResponseEntity.noContent().build();
  }

}
