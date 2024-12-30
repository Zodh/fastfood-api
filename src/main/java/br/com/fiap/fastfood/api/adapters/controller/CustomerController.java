package br.com.fiap.fastfood.api.adapters.controller;

import br.com.fiap.fastfood.api.adapters.gateway.ActivationCodeLinkGeneratorGatewayImpl;
import br.com.fiap.fastfood.api.adapters.gateway.EmailSenderGatewayImpl;
import br.com.fiap.fastfood.api.adapters.gateway.CustomerRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.gateway.ActivationCodeRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.application.dto.customer.DocumentTypeEnum;
import br.com.fiap.fastfood.api.application.gateway.mapper.ActivationCodeMapperAppImpl;
import br.com.fiap.fastfood.api.application.gateway.mapper.CustomerMapperAppImpl;
import br.com.fiap.fastfood.api.application.usecase.impl.CustomerUseCaseImpl;
import br.com.fiap.fastfood.api.application.usecase.CustomerUseCase;

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
