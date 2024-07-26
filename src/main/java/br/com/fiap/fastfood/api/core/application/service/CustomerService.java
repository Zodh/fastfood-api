package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.domain.aggregate.ServiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.Document;
import br.com.fiap.fastfood.api.core.domain.ports.outbound.ActivationCodeLinkGeneratorPort;
import br.com.fiap.fastfood.api.core.domain.ports.outbound.EmailSenderPort;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.ActivationCodeRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.CustomerRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.service.ActivationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {

  private final CustomerRepositoryPort customerRepositoryPort;
  private final EmailSenderPort emailSenderPort;
  private final ActivationCodeService activationCodeService;

  @Autowired
  public CustomerService(
      CustomerRepositoryPort customerRepositoryPort,
      EmailSenderPort emailSenderPort,
      ActivationCodeRepositoryPort activationCodeRepository,
      ActivationCodeLinkGeneratorPort activationCodeLinkGeneratorPort
  ) {
    this.customerRepositoryPort = customerRepositoryPort;
    this.emailSenderPort = emailSenderPort;
    this.activationCodeService = new ActivationCodeService(activationCodeRepository, activationCodeLinkGeneratorPort, customerRepositoryPort);
  }

  public void register(Customer customer) {
    ServiceAggregate serviceAggregate = new ServiceAggregate(
        customer,
        customerRepositoryPort,
        emailSenderPort,
        activationCodeService
    );
    serviceAggregate.register();
  }

  public void activate(UUID code) {
    activationCodeService.activate(code);
  }

  public Customer identify(String documentNumber) {
    ServiceAggregate serviceAggregate = new ServiceAggregate(
            new Document(documentNumber),
            customerRepositoryPort
    );
    return serviceAggregate.identify();
  }
  public void resendVerificationLink(String email) {
    ServiceAggregate serviceAggregate = new ServiceAggregate(email, customerRepositoryPort, activationCodeService, emailSenderPort);
    serviceAggregate.resendVerificationLink();
  }

}
