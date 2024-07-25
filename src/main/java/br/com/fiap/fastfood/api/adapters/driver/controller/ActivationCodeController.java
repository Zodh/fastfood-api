package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.core.application.service.CustomerService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activation-code")
public class ActivationCodeController {

  private final CustomerService customerService;

  @Autowired
  public ActivationCodeController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping(value = "/{code}")
  public ResponseEntity<Void> activate(@PathVariable UUID code) {
    customerService.activate(code);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
