package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CustomerMapper;
import br.com.fiap.fastfood.api.adapters.driver.dto.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.service.CustomerService;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  private final CustomerService customerService;
  private final CustomerMapper mapper;

  @Autowired
  public CustomerController(CustomerService customerService, CustomerMapper mapper) {
    this.customerService = customerService;
    this.mapper = mapper;
  }

  @PostMapping
  public ResponseEntity<Void> register(@RequestBody CustomerDTO customer) {
    Customer domain = mapper.toDomain(customer);
    customerService.register(domain);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
