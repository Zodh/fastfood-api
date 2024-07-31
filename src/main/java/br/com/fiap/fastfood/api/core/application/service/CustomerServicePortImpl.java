package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.ports.outbound.ActivationCodeLinkGeneratorPort;
import br.com.fiap.fastfood.api.core.application.ports.repository.ActivationCodeRepositoryPort;
import br.com.fiap.fastfood.api.core.application.ports.repository.CustomerRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.ServiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.PersonValidator;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.Document;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.CustomerServicePort;
import br.com.fiap.fastfood.api.core.domain.ports.outbound.EmailSenderPort;
import com.github.dockerjava.api.exception.NotFoundException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServicePortImpl implements CustomerServicePort {

  private final CustomerRepositoryPort repository;
  private final EmailSenderPort emailSender;
  private final ActivationCodeService activationCodeService;
  private final PersonValidator personValidator;

  @Autowired
  public CustomerServicePortImpl(
      CustomerRepositoryPort repository,
      EmailSenderPort emailSender,
      ActivationCodeRepositoryPort activationCodeRepository,
      ActivationCodeLinkGeneratorPort activationCodeLinkGeneratorPort
  ) {
    this.repository = repository;
    this.emailSender = emailSender;
    this.activationCodeService = new ActivationCodeService(activationCodeRepository,
        activationCodeLinkGeneratorPort,
        repository);
    this.personValidator = new PersonValidator();
  }

  private Customer findByEmail(String email) {
    return repository.findByEmail(email).orElseThrow(
        () -> new NotFoundException("Não foi encontrado um cliente com o email informado!"));
  }

  public void register(Customer customer) {
    boolean isEmailUsed = repository.findByEmail(customer.getEmail().getValue()).isPresent();
    boolean isDocumentUsed = repository.findByDocument(customer.getDocument()).isPresent();
    ServiceAggregate serviceAggregate = new ServiceAggregate(
        customer,
        emailSender,
        personValidator
    );
    serviceAggregate.register(isEmailUsed, isDocumentUsed);
    Customer persistedCustomer = repository.save(customer);
    String activationCode = activationCodeService.generate(persistedCustomer);
    serviceAggregate.sendActivationCode(customer, activationCode);
  }

  @Override
  public void activate(UUID code) {
    activationCodeService.activate(code);
  }

  @Override
  public Customer identify(String documentNumber) {
    Customer customer = new Customer();
    Document document = new Document(documentNumber);
    customer.setDocument(document);
    ServiceAggregate serviceAggregate = new ServiceAggregate(customer);
    serviceAggregate.checkDocument();
    return repository.findByDocument(document).orElseThrow(() -> new NotFoundException(
        "Não foi encontrado um cliente cadastrado com esse documento!"));
  }

  @Override
  public void resendActivationCode(String email) {
    Customer customer = this.findByEmail(email);
    ServiceAggregate serviceAggregate = new ServiceAggregate(customer, emailSender);
    serviceAggregate.canResendActivationCode();
    String activationCode = activationCodeService.generate(customer);
    serviceAggregate.sendActivationCode(customer, activationCode);
  }

}
