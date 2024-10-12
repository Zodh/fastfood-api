package br.com.fiap.fastfood.api.adapters.controller;

import br.com.fiap.fastfood.api.adapters.gateway.ActivationCodeLinkGeneratorGatewayImpl;
import br.com.fiap.fastfood.api.adapters.gateway.EmailSenderGatewayImpl;
import br.com.fiap.fastfood.api.adapters.gateway.CustomerRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.gateway.ActivationCodeRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.application.gateway.mapper.ActivationCodeMapperAppImpl;
import br.com.fiap.fastfood.api.application.gateway.mapper.CustomerMapperAppImpl;
import br.com.fiap.fastfood.api.application.usecase.impl.CustomerUseCaseImpl;
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


public class ActivationCodeController {

  private final CustomerUseCaseImpl customerUseCaseImpl;


  public ActivationCodeController(CustomerRepositoryAdapterImpl customerRepositoryAdapter, EmailSenderGatewayImpl emailSenderGatewayImpl, ActivationCodeRepositoryAdapterImpl activationCodeRepositoryAdapter, ActivationCodeLinkGeneratorGatewayImpl activationCodeLinkGeneratorGatewayImpl) {
    this.customerUseCaseImpl = new CustomerUseCaseImpl(customerRepositoryAdapter,
        emailSenderGatewayImpl, activationCodeRepositoryAdapter,
        activationCodeLinkGeneratorGatewayImpl, new ActivationCodeMapperAppImpl(), new CustomerMapperAppImpl());
  }


  public void activate(UUID code) {
    customerUseCaseImpl.activate(code);
  }


  public void generate(String email) {
    customerUseCaseImpl.resendActivationCode(email);
  }

}
