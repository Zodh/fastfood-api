package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.email.ActivationCodeLinkGenerator;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.email.EmailSender;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.CustomerRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.FollowUpRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.InvoiceRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.MenuProductRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.OrderProductRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.OrderRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.ActivationCodeRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.CreateOrderRequestDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.PaidOrderResponseDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.core.application.policy.FollowUpPolicyImpl;
import br.com.fiap.fastfood.api.core.application.policy.OrderInvoicePolicyImpl;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.InvoiceServicePort;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.OrderServicePort;
import br.com.fiap.fastfood.api.core.application.service.CustomerServicePortImpl;
import br.com.fiap.fastfood.api.core.application.service.InvoiceServicePortImpl;
import br.com.fiap.fastfood.api.core.application.service.MenuProductServicePortImpl;
import br.com.fiap.fastfood.api.core.application.service.OrderProductServicePortImpl;
import br.com.fiap.fastfood.api.core.application.service.OrderServicePortImpl;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderServicePort orderServicePort;
  private final InvoiceServicePort invoiceServicePort;

  @Autowired
  public OrderController(
      OrderRepositoryAdapterImpl orderRepositoryAdapter,
      CustomerRepositoryAdapterImpl customerRepositoryAdapter,
      EmailSender emailSenderAdapter,
      ActivationCodeRepositoryAdapterImpl activationCodeRepositoryAdapter,
      ActivationCodeLinkGenerator activationCodeLinkGenerator,
      OrderProductRepositoryAdapterImpl orderProductRepositoryAdapter,
      MenuProductRepositoryAdapterImpl menuProductRepositoryAdapter,
      InvoiceRepositoryAdapterImpl invoiceRepositoryAdapter,
      FollowUpRepositoryAdapterImpl followUpRepositoryAdapter
  ) {
    CustomerServicePortImpl customerServicePortImpl = new CustomerServicePortImpl(
        customerRepositoryAdapter, emailSenderAdapter, activationCodeRepositoryAdapter,
        activationCodeLinkGenerator);
    MenuProductServicePortImpl menuProductServicePortImpl = new MenuProductServicePortImpl(
        menuProductRepositoryAdapter);
    OrderProductServicePortImpl orderProductServicePort = new OrderProductServicePortImpl(
        orderProductRepositoryAdapter, menuProductServicePortImpl);
    FollowUpPolicyImpl followUpPolicy = new FollowUpPolicyImpl(followUpRepositoryAdapter);
    OrderInvoicePolicyImpl orderInvoicePolicyPort = new OrderInvoicePolicyImpl(
        invoiceRepositoryAdapter, orderRepositoryAdapter, followUpPolicy);
    this.orderServicePort = new OrderServicePortImpl(orderRepositoryAdapter,
        customerServicePortImpl, orderProductServicePort, orderInvoicePolicyPort, emailSenderAdapter, followUpPolicy);
    this.invoiceServicePort = new InvoiceServicePortImpl(invoiceRepositoryAdapter,
        orderInvoicePolicyPort);
  }

  @GetMapping
  public ResponseEntity<List<OrderDTO>> findAll() {
    return ResponseEntity.ok(orderServicePort.findAll());
  }

  @PostMapping
  public ResponseEntity<OrderDTO> create(
      @RequestBody(required = false) CreateOrderRequestDTO request) {
    OrderDTO orderDTO = orderServicePort.create(request.getCustomer(), request.getCollaborator());
    return ResponseEntity.status(HttpStatus.OK).body(orderDTO);
  }

  @PostMapping("/{id}/products")
  public ResponseEntity<OrderDTO> includeOrderProduct(@PathVariable Long id, @RequestBody
  OrderProductDTO orderProductDTO) {
    OrderDTO order = orderServicePort.includeOrderProduct(id, orderProductDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(order);
  }

  @DeleteMapping("/{id}/products/{orderProductId}")
  public ResponseEntity<OrderDTO> removeOrderProduct(@PathVariable Long id,
      @PathVariable Long orderProductId) {
    OrderDTO orderDTO = orderServicePort.removeOrderProduct(id, orderProductId);
    return ResponseEntity.status(HttpStatus.OK).body(orderDTO);
  }

  @PatchMapping("/{id}/confirm")
  public ResponseEntity<OrderDTO> confirmOrder(@PathVariable Long id) {
    OrderDTO confirmedOrder = orderServicePort.confirm(id);
    return ResponseEntity.status(HttpStatus.OK).body(confirmedOrder);
  }

  @PatchMapping("/{id}/pay")
  public ResponseEntity<PaidOrderResponseDTO> executeFakeCheckout(@PathVariable Long id) {
    OrderDTO orderDTO = orderServicePort.getById(id);
    invoiceServicePort.executeFakeCheckout(orderDTO);
    PaidOrderResponseDTO result = PaidOrderResponseDTO.builder().orderNumber(orderDTO.getId())
        .build();
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @GetMapping("/{id}/invoice")
  public ResponseEntity<InvoiceDTO> getOrderInvoice(@PathVariable Long id) {
    InvoiceDTO orderInvoice = orderServicePort.getById(id).getInvoice();
    return ResponseEntity.status(HttpStatus.OK).body(orderInvoice);
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

  @PatchMapping("/{id}/ready")
  public ResponseEntity<Void> turnOrderReadyToPick(@PathVariable Long id) {
    orderServicePort.turnReadyToPick(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/finish")
  public ResponseEntity<Void> finishOrder(@PathVariable Long id) {
    orderServicePort.finish(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/products/{orderProductId}/optionals")
  public ResponseEntity<OrderDTO> includeOptional(
      @PathVariable Long id,
      @PathVariable Long orderProductId,
      @RequestBody OrderProductDTO optionalDTO
  ) {
    OrderDTO result = orderServicePort.includeOptional(id, orderProductId, optionalDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @DeleteMapping("/{id}/products/{orderProductId}/optionals/{optionalId}")
  public ResponseEntity<OrderDTO> removeOptional(
      @PathVariable Long id,
      @PathVariable Long orderProductId,
      @PathVariable Long optionalId
  ) {
    OrderDTO orderDTO = orderServicePort.removeOptional(id, orderProductId, optionalId);
    return ResponseEntity.status(HttpStatus.OK).body(orderDTO);
  }

  @PatchMapping("/{id}/products/{orderProductId}/ingredients/{ingredientId}")
  public ResponseEntity<OrderDTO> updateShouldRemove(
      @PathVariable Long id,
      @PathVariable Long orderProductId,
      @PathVariable Long ingredientId,
      @RequestParam boolean shouldRemove
  ) {
    OrderDTO orderDTO = orderServicePort.updateShouldRemoveIngredient(id, orderProductId, ingredientId, shouldRemove);
    return ResponseEntity.status(HttpStatus.OK).body(orderDTO);
  }

}
