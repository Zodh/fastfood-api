package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CustomerMapper;
import br.com.fiap.fastfood.api.adapters.driver.dto.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.service.CustomerService;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;
  private final CustomerMapper mapper;

  @PostMapping
  public ResponseEntity<Void> register(@RequestBody CustomerDTO customer) {
    Customer domainCustomer = mapper.toDomain(customer);
    customerService.register(domainCustomer);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
