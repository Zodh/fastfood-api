package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CustomerMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.IdentifyCustomerMapper;
import br.com.fiap.fastfood.api.adapters.driver.dto.CustomerDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.IdentifyCustomerDTO;
import br.com.fiap.fastfood.api.core.application.service.CustomerService;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper mapper;
    private final IdentifyCustomerMapper identifyMapper;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerMapper mapper, IdentifyCustomerMapper identifyMapper) {
        this.customerService = customerService;
        this.mapper = mapper;
        this.identifyMapper = identifyMapper;
    }

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody CustomerDTO customer) {
        Customer domain = mapper.toDomain(customer);
        customerService.register(domain);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{documentNumber}")
    public ResponseEntity<IdentifyCustomerDTO> identify(@PathVariable String documentNumber) {
        Customer identifiedCustomer = customerService.identify(documentNumber);
        IdentifyCustomerDTO identifyCustomerDTO = identifyMapper.toIdentifyDTO(identifiedCustomer);

        return ResponseEntity.status(HttpStatus.OK).body(identifyCustomerDTO);
    }

}
