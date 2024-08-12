package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.dto.customer.DocumentTypeEnum;
import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.mapper.CustomerMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.CustomerMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.port.outbound.ActivationCodeLinkGeneratorPort;
import br.com.fiap.fastfood.api.core.application.port.repository.ActivationCodeRepositoryPort;
import br.com.fiap.fastfood.api.core.application.port.repository.CustomerRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.ServiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.PersonValidator;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.Document;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.CustomerServicePort;
import br.com.fiap.fastfood.api.core.domain.port.outbound.EmailSenderPort;
import java.util.UUID;

public class CustomerServicePortImpl implements CustomerServicePort {

  private final CustomerRepositoryPort repository;
  private final EmailSenderPort emailSender;
  private final ActivationCodeService activationCodeService;
  private final PersonValidator personValidator;
  private final CustomerMapperApp customerMapperApp;

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
    this.customerMapperApp = new CustomerMapperAppImpl();
  }

  private CustomerDTO findByEmail(String email) {
    return repository.findByEmail(email).orElseThrow(
        () -> new NotFoundException("Não foi encontrado um cliente com o email informado!"));
  }

  public void register(CustomerDTO customerDTO) {
    boolean isEmailUsed = repository.findByEmail(customerDTO.getEmail()).isPresent();
    boolean isDocumentUsed = repository.findByDocument(customerDTO.getDocumentNumber(),
        customerDTO.getDocumentType()).isPresent();
    Customer customer = customerMapperApp.toDomain(customerDTO);
    ServiceAggregate serviceAggregate = new ServiceAggregate(
        customer,
        emailSender,
        personValidator
    );
    serviceAggregate.register(isEmailUsed, isDocumentUsed);
    CustomerDTO registeredCustomer = customerMapperApp.toDTO(customer);
    CustomerDTO persistedCustomer = repository.save(registeredCustomer);
    String activationCode = activationCodeService.generate(persistedCustomer);
    Customer result = customerMapperApp.toDomain(persistedCustomer);
    serviceAggregate.sendActivationCode(result, activationCode);
  }

  @Override
  public void activate(UUID code) {
    activationCodeService.activate(code);
  }

  @Override
  public CustomerDTO identify(String documentNumber, DocumentTypeEnum documentType) {
    Customer customer = new Customer();
    Document document = new Document(documentNumber, documentType.name());
    customer.setDocument(document);
    ServiceAggregate serviceAggregate = new ServiceAggregate(customer);
    serviceAggregate.checkDocument();
    DocumentTypeEnum documentTypeEnum = DocumentTypeEnum.valueOf(document.getType().name());
    return repository.findByDocument(documentNumber, documentTypeEnum).orElseThrow(() -> new NotFoundException(
        "Não foi encontrado um cliente cadastrado com esse documento!"));
  }

  @Override
  public void resendActivationCode(String email) {
    CustomerDTO customerDTO = this.findByEmail(email);
    Customer customer = customerMapperApp.toDomain(customerDTO);
    ServiceAggregate serviceAggregate = new ServiceAggregate(customer, emailSender);
    serviceAggregate.canResendActivationCode();
    CustomerDTO validCustomer = customerMapperApp.toDTO(customer);
    String activationCode = activationCodeService.generate(validCustomer);
    serviceAggregate.sendActivationCode(customer, activationCode);
  }

  @Override
  public CustomerDTO getById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Não foi encontrado um cliente com o identificador %d!", id)));
  }

  @Override
  public CustomerDTO getByDocument(String documentNumber, DocumentTypeEnum documentType) {
    return repository.findByDocument(documentNumber, documentType).orElseThrow(() -> new NotFoundException(String.format("Não foi encontrado um cliente com o documento %s %s!", documentType, documentNumber)));
  }

}
