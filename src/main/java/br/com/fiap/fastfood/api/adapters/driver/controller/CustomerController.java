package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CustomerIdentityMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CustomerMapper;
import br.com.fiap.fastfood.api.adapters.driver.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.customer.CustomerIdentityDTO;
import br.com.fiap.fastfood.api.core.application.service.CustomerServicePortImpl;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.CustomerServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  private final CustomerServicePort customerServicePort;
  private final CustomerMapper mapper;
  private final CustomerIdentityMapper identifyMapper;

  @Autowired
  public CustomerController(CustomerServicePortImpl customerServicePortImpl, CustomerMapper mapper,
      CustomerIdentityMapper identifyMapper) {
    this.customerServicePort = customerServicePortImpl;
    this.mapper = mapper;
    this.identifyMapper = identifyMapper;
  }

  @PostMapping
  public ResponseEntity<Void> register(@RequestBody CustomerDTO customer) {
    Customer domain = mapper.toDomain(customer);
    customerServicePort.register(domain);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{documentNumber}")
  public ResponseEntity<CustomerIdentityDTO> identify(@PathVariable String documentNumber) {
    Customer identifiedCustomer = customerServicePort.identify(documentNumber);
    CustomerIdentityDTO customerIdentityDTO = identifyMapper.toIdentityDTO(identifiedCustomer);

    return ResponseEntity.status(HttpStatus.OK).body(customerIdentityDTO);
  }

}
