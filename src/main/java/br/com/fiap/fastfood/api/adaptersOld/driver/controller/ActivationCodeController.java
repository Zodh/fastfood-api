package br.com.fiap.fastfood.api.adaptersOld.driver.controller;

import br.com.fiap.fastfood.api.adaptersOld.driven.infrastructure.email.ActivationCodeLinkGenerator;
import br.com.fiap.fastfood.api.adaptersOld.driven.infrastructure.email.EmailSender;
import br.com.fiap.fastfood.api.adaptersOld.driven.infrastructure.repository.adapter.CustomerRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adaptersOld.driven.infrastructure.repository.adapter.ActivationCodeRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.application.usecase.CustomerUseCase;
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

  private final CustomerUseCase customerUseCase;

  @Autowired
  public ActivationCodeController(CustomerRepositoryAdapterImpl customerRepositoryAdapter, EmailSender emailSender, ActivationCodeRepositoryAdapterImpl activationCodeRepositoryAdapter, ActivationCodeLinkGenerator activationCodeLinkGenerator) {
    this.customerUseCase = new CustomerUseCase(customerRepositoryAdapter, emailSender, activationCodeRepositoryAdapter, activationCodeLinkGenerator);
  }

  @GetMapping(value = "/{code}")
  public ResponseEntity<Void> activate(@PathVariable UUID code) {
    customerUseCase.activate(code);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping(value = "/generate")
  public ResponseEntity<Void> generate(@RequestParam(value = "email") String email) {
    customerUseCase.resendActivationCode(email);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
