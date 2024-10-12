package br.com.fiap.fastfood.api.infrastructure.dao.web;

import br.com.fiap.fastfood.api.adapters.controller.CustomerController;
import br.com.fiap.fastfood.api.adapters.gateway.ActivationCodeLinkGeneratorGatewayImpl;
import br.com.fiap.fastfood.api.adapters.gateway.ActivationCodeRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.gateway.CustomerRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.gateway.EmailSenderGatewayImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerApi {

    private final CustomerController customerController;

    @Autowired
    public CustomerApi(CustomerRepositoryAdapterImpl customerRepositoryAdapter,
                       EmailSenderGatewayImpl emailSenderGatewayImpl, ActivationCodeRepositoryAdapterImpl activationCodeRepositoryAdapter,
                       ActivationCodeLinkGeneratorGatewayImpl activationCodeLinkGeneratorGatewayImpl) {
        this.customerController = new CustomerController(customerRepositoryAdapter, emailSenderGatewayImpl, activationCodeRepositoryAdapter, activationCodeLinkGeneratorGatewayImpl);
    }


    @PostMapping
    public ResponseEntity<Void> register(@RequestBody br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO customer) {
        customerController.register(customer);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO> identify(@RequestParam String documentNumber,
                                                                                                       @RequestParam br.com.fiap.fastfood.api.core.application.dto.customer.DocumentTypeEnum documentType) {
        return ResponseEntity.status(HttpStatus.OK).body(customerController.identify(documentNumber, documentType));
    }
}
