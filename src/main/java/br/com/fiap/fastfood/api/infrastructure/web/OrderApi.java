package br.com.fiap.fastfood.api.infrastructure.web;

import br.com.fiap.fastfood.api.adapters.controller.OrderController;
import br.com.fiap.fastfood.api.adapters.gateway.*;
import br.com.fiap.fastfood.api.adapters.gateway.link.ApplicationServerLinkGenerator;
import br.com.fiap.fastfood.api.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.application.dto.order.CreateOrderRequestDTO;
import br.com.fiap.fastfood.api.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.application.dto.order.PaidOrderResponseDTO;
import br.com.fiap.fastfood.api.application.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.infrastructure.config.PaymentApiConfig;
import java.awt.image.BufferedImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.client.RestTemplate;

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
        FollowUpRepositoryAdapterImpl followUpRepositoryAdapter,
        RestTemplate restTemplate,
        PaymentApiConfig paymentApiConfig,
        ApplicationServerLinkGenerator applicationServerLinkGenerator) {
        this.orderController = new OrderController(orderRepositoryAdapter,
            customerRepositoryAdapter, emailSenderGatewayImplAdapter,
            activationCodeRepositoryAdapter, activationCodeLinkGeneratorGatewayImpl
            , orderProductRepositoryAdapter, menuProductRepositoryAdapter, invoiceRepositoryAdapter,
            followUpRepositoryAdapter, restTemplate, paymentApiConfig,
            applicationServerLinkGenerator);

    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> findAll() {
        return ResponseEntity.ok(orderController.findAll());
    }

    @PostMapping
    public ResponseEntity<OrderDTO> create(
            @RequestBody(required = false) CreateOrderRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(orderController.create(request));
    }

    @PostMapping("/{id}/products")
    public ResponseEntity<OrderDTO> includeOrderProduct(@PathVariable Long id, @RequestBody
    OrderProductDTO orderProductDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderController.includeOrderProduct(id, orderProductDTO));
    }

    @DeleteMapping("/{id}/products/{orderProductId}")
    public ResponseEntity<OrderDTO> removeOrderProduct(@PathVariable Long id,
                                                                                                           @PathVariable Long orderProductId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderController.removeOrderProduct(id, orderProductId));
    }

    @PatchMapping(value = "/{id}/confirm", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<BufferedImage> confirmOrder(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderController.confirmOrder(id));
    }

    @GetMapping("/{id}/pay")
    public ResponseEntity<PaidOrderResponseDTO> executeFakeCheckout(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderController.executeFakeCheckout(id));
    }

    @GetMapping("/{id}/invoice")
    public ResponseEntity<InvoiceDTO> getOrderInvoice(@PathVariable Long id) {
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
    public ResponseEntity<OrderDTO> includeOptional(
            @PathVariable Long id,
            @PathVariable Long orderProductId,
            @RequestBody OrderProductDTO optionalDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderController.includeOptional(id, orderProductId, optionalDTO));
    }

    @DeleteMapping("/{id}/products/{orderProductId}/optionals/{optionalId}")
    public ResponseEntity<OrderDTO> removeOptional(
            @PathVariable Long id,
            @PathVariable Long orderProductId,
            @PathVariable Long optionalId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(orderController.removeOptional(id, orderProductId, optionalId));
    }

    @PatchMapping("/{id}/products/{orderProductId}/ingredients/{ingredientId}")
    public ResponseEntity<OrderDTO> updateShouldRemove(
            @PathVariable Long id,
            @PathVariable Long orderProductId,
            @PathVariable Long ingredientId,
            @RequestParam boolean shouldRemove
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(orderController.updateShouldRemove(id, orderProductId, ingredientId, shouldRemove));
    }
}
