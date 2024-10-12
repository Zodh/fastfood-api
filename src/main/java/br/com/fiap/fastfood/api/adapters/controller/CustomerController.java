package br.com.fiap.fastfood.api.adapters.controller;

import br.com.fiap.fastfood.api.adapters.gateway.ActivationCodeLinkGeneratorGatewayImpl;
import br.com.fiap.fastfood.api.adapters.gateway.EmailSenderGatewayImpl;
import br.com.fiap.fastfood.api.adapters.gateway.CustomerRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.gateway.ActivationCodeRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.application.gateway.mapper.ActivationCodeMapperAppImpl;
import br.com.fiap.fastfood.api.application.gateway.mapper.CustomerMapperAppImpl;
import br.com.fiap.fastfood.api.application.usecase.impl.CustomerUseCaseImpl;
import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.dto.customer.DocumentTypeEnum;
import br.com.fiap.fastfood.api.application.usecase.CustomerUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


public class CustomerController {

  private final CustomerUseCase customerUseCase;


  public CustomerController(CustomerRepositoryAdapterImpl customerRepositoryAdapter,
      EmailSenderGatewayImpl emailSenderGatewayImpl, ActivationCodeRepositoryAdapterImpl activationCodeRepositoryAdapter,
      ActivationCodeLinkGeneratorGatewayImpl activationCodeLinkGeneratorGatewayImpl) {
    this.customerUseCase = new CustomerUseCaseImpl(customerRepositoryAdapter,
        emailSenderGatewayImpl,
        activationCodeRepositoryAdapter, activationCodeLinkGeneratorGatewayImpl, new ActivationCodeMapperAppImpl(), new CustomerMapperAppImpl());
  }


  public void register(CustomerDTO customer) {
    customerUseCase.register(customer);
  }


  public CustomerDTO identify(String documentNumber,
      DocumentTypeEnum documentType) {
    return customerUseCase.identify(documentNumber, documentType);

  }

}
