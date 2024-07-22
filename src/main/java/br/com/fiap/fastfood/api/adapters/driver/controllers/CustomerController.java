package br.com.fiap.fastfood.api.adapters.driver.controllers;

import br.com.fiap.fastfood.api.adapters.driver.mapper.CustomerMapper;
import br.com.fiap.fastfood.api.adapters.driver.dto.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.aggregate.service.ServiceAggregateInboundPort;
import br.com.fiap.fastfood.api.core.domain.entity.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  private ServiceAggregateInboundPort serviceAggregateInboundPort;
  private CustomerMapper mapper;

  @PostMapping
  public ResponseEntity<Void> register(@RequestBody CustomerDTO customer) {
    Customer domainCustomer = mapper.toDomain(customer);
    serviceAggregateInboundPort.register(domainCustomer);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
