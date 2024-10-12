package br.com.fiap.fastfood.api.infrastructure.dao.web;

import br.com.fiap.fastfood.api.adapters.controller.OrderController;
import br.com.fiap.fastfood.api.adapters.gateway.*;
import br.com.fiap.fastfood.api.application.dto.product.OrderProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApi {


    private final OrderController orderController;


    @Autowired
    public OrderApi(
                                 OrderRepositoryAdapterImpl orderRepositoryAdapter,
                                 CustomerRepositoryAdapterImpl customerRepositoryAdapter,
                                 EmailSenderGatewayImpl emailSenderGatewayImplAdapter,
                                 ActivationCodeRepositoryAdapterImpl activationCodeRepositoryAdapter,
                                 ActivationCodeLinkGeneratorGatewayImpl activationCodeLinkGeneratorGatewayImpl,
                                 OrderProductRepositoryAdapterImpl orderProductRepositoryAdapter,
                                 MenuProductRepositoryGatewayImpl menuProductRepositoryAdapter,
                                 InvoiceRepositoryAdapterImpl invoiceRepositoryAdapter,
                                 FollowUpRepositoryAdapterImpl followUpRepositoryAdapter) {
        this.orderController = new OrderController(orderRepositoryAdapter, customerRepositoryAdapter, emailSenderGatewayImplAdapter, activationCodeRepositoryAdapter, activationCodeLinkGeneratorGatewayImpl
        , orderProductRepositoryAdapter, menuProductRepositoryAdapter, invoiceRepositoryAdapter, followUpRepositoryAdapter);

    }

    @GetMapping
    public ResponseEntity<List<br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO>> findAll() {
        return ResponseEntity.ok(orderController.findAll());
    }

    @PostMapping
    public ResponseEntity<br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO> create(
            @RequestBody(required = false) br.com.fiap.fastfood.api.core.application.dto.order.CreateOrderRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(orderController.create(request));
    }

    @PostMapping("/{id}/products")
    public ResponseEntity<br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO> includeOrderProduct(@PathVariable Long id, @RequestBody
    OrderProductDTO orderProductDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderController.includeOrderProduct(id, orderProductDTO));
    }

    @DeleteMapping("/{id}/products/{orderProductId}")
    public ResponseEntity<br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO> removeOrderProduct(@PathVariable Long id,
                                                                                                           @PathVariable Long orderProductId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderController.removeOrderProduct(id, orderProductId));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO> confirmOrder(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderController.confirmOrder(id));
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<br.com.fiap.fastfood.api.core.application.dto.order.PaidOrderResponseDTO> executeFakeCheckout(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderController.executeFakeCheckout(id));
    }

    @GetMapping("/{id}/invoice")
    public ResponseEntity<br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO> getOrderInvoice(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderController.getOrderInvoice(id));
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
       orderController.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/prepare")
    public ResponseEntity<Void> startOrderPreparation(@PathVariable Long id) {
        orderController.startOrderPreparation(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ready")
    public ResponseEntity<Void> turnOrderReadyToPick(@PathVariable Long id) {
        orderController.turnOrderReadyToPick(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<Void> finishOrder(@PathVariable Long id) {
        orderController.finishOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/products/{orderProductId}/optionals")
    public ResponseEntity<br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO> includeOptional(
            @PathVariable Long id,
            @PathVariable Long orderProductId,
            @RequestBody OrderProductDTO optionalDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderController.includeOptional(id, orderProductId, optionalDTO));
    }

    @DeleteMapping("/{id}/products/{orderProductId}/optionals/{optionalId}")
    public ResponseEntity<br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO> removeOptional(
            @PathVariable Long id,
            @PathVariable Long orderProductId,
            @PathVariable Long optionalId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(orderController.removeOptional(id, orderProductId, optionalId));
    }

    @PatchMapping("/{id}/products/{orderProductId}/ingredients/{ingredientId}")
    public ResponseEntity<br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO> updateShouldRemove(
            @PathVariable Long id,
            @PathVariable Long orderProductId,
            @PathVariable Long ingredientId,
            @RequestParam boolean shouldRemove
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(orderController.updateShouldRemove(id, orderProductId, ingredientId, shouldRemove));
    }
}
