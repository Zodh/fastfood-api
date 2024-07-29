package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.core.application.service.CustomerServiceImpl;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activation-code")
public class ActivationCodeController {

  private final CustomerServiceImpl customerServiceImpl;

  @Autowired
  public ActivationCodeController(CustomerServiceImpl customerServiceImpl) {
    this.customerServiceImpl = customerServiceImpl;
  }

  @GetMapping(value = "/{code}")
  public ResponseEntity<Void> activate(@PathVariable UUID code) {
    customerServiceImpl.activate(code);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping(value = "/generate")
  public ResponseEntity<Void> generate(@RequestParam(value = "email") String email) {
    customerServiceImpl.resendVerificationLink(email);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
