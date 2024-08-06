package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.core.application.service.InvoiceServicePortImpl;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.InvoiceServicePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceServicePort invoiceServicePort;

    public InvoiceController(InvoiceServicePortImpl invoiceServicePort) {
        this.invoiceServicePort = invoiceServicePort;
    }

    @PostMapping("/orders/{orderId}/pay")
    public ResponseEntity<Void> executeFakeCheckout(@PathVariable Long orderId) {
        invoiceServicePort.executeFakeCheckout(orderId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
