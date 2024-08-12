package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.email.ActivationCodeLinkGenerator;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.email.EmailSender;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.CustomerRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.ActivationCodeRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.dto.customer.DocumentTypeEnum;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.CustomerServicePort;
import br.com.fiap.fastfood.api.core.application.service.CustomerServicePortImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  private final CustomerServicePort customerServicePort;

  @Autowired
  public CustomerController(CustomerRepositoryAdapterImpl customerRepositoryAdapter,
      EmailSender emailSender, ActivationCodeRepositoryAdapterImpl activationCodeRepositoryAdapter,
      ActivationCodeLinkGenerator activationCodeLinkGenerator) {
    this.customerServicePort = new CustomerServicePortImpl(customerRepositoryAdapter, emailSender,
        activationCodeRepositoryAdapter, activationCodeLinkGenerator);
  }

  @PostMapping
  public ResponseEntity<Void> register(@RequestBody CustomerDTO customer) {
    customerServicePort.register(customer);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping
  public ResponseEntity<CustomerDTO> identify(@RequestParam String documentNumber,
      @RequestParam DocumentTypeEnum documentType) {
    CustomerDTO customerDTO = customerServicePort.identify(documentNumber, documentType);
    return ResponseEntity.status(HttpStatus.OK).body(customerDTO);
  }

}
