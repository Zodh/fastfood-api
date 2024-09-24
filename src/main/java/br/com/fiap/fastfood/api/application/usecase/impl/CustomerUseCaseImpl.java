package br.com.fiap.fastfood.api.application.usecase.impl;

import br.com.fiap.fastfood.api.application.gateway.mapper.ActivationCodeMapperApp;
import br.com.fiap.fastfood.api.application.usecase.CustomerUseCase;
import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.dto.customer.DocumentTypeEnum;
import br.com.fiap.fastfood.api.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.application.gateway.mapper.CustomerMapperApp;
import br.com.fiap.fastfood.api.application.gateway.ActivationCodeLinkGeneratorGateway;
import br.com.fiap.fastfood.api.application.gateway.repository.ActivationCodeRepositoryGateway;
import br.com.fiap.fastfood.api.application.gateway.repository.CustomerRepositoryGateway;
import br.com.fiap.fastfood.api.entities.exception.DomainException;
import br.com.fiap.fastfood.api.entities.exception.ErrorDetail;
import br.com.fiap.fastfood.api.application.gateway.EmailSenderGateway;
import br.com.fiap.fastfood.api.entities.person.Customer;
import br.com.fiap.fastfood.api.entities.person.PersonValidator;
import br.com.fiap.fastfood.api.entities.person.vo.Document;

import java.util.List;
import java.util.UUID;

public class CustomerUseCaseImpl implements
    CustomerUseCase {

  private final CustomerRepositoryGateway repository;
  private final EmailSenderGateway emailSender;
  private final ActivationCodeUseCaseImpl activationCodeUseCaseImpl;
  private final CustomerMapperApp customerMapperApp;

  public CustomerUseCaseImpl(
      CustomerRepositoryGateway repository,
      EmailSenderGateway emailSender,
      ActivationCodeRepositoryGateway activationCodeRepository,
      ActivationCodeLinkGeneratorGateway activationCodeLinkGeneratorGateway,
      ActivationCodeMapperApp activationCodeMapperApp,
      CustomerMapperApp customerMapperApp
  ) {
    this.repository = repository;
    this.emailSender = emailSender;
    this.activationCodeUseCaseImpl = new ActivationCodeUseCaseImpl(activationCodeMapperApp, customerMapperApp, activationCodeRepository,
        activationCodeLinkGeneratorGateway,
        repository);
    this.customerMapperApp = customerMapperApp;
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

    List<ErrorDetail> errorDetails = new PersonValidator().validate(customer);
    if (!errorDetails.isEmpty()) {
      throw new DomainException(errorDetails);
    }

    this.validateEmailUsage(isEmailUsed);
    this.validateDocumentNumberUsage(isDocumentUsed);
    customer.setActive(false);

    CustomerDTO registeredCustomer = customerMapperApp.toDTO(customer);
    CustomerDTO persistedCustomer = repository.save(registeredCustomer);
    String activationCode = activationCodeUseCaseImpl.generate(persistedCustomer);
    Customer result = customerMapperApp.toDomain(persistedCustomer);

    sendActivationCode(result, activationCode);
  }

  @Override
  public void activate(UUID code) {
    activationCodeUseCaseImpl.activate(code);
  }

  @Override
  public CustomerDTO identify(String documentNumber, DocumentTypeEnum documentType) {
    Customer customer = new Customer();
    Document document = new Document(documentNumber, documentType.name());
    customer.setDocument(document);
    customer.checkDocument();
    DocumentTypeEnum documentTypeEnum = DocumentTypeEnum.valueOf(document.getType().name());
    return repository.findByDocument(documentNumber, documentTypeEnum).orElseThrow(() -> new NotFoundException(
        "Não foi encontrado um cliente cadastrado com esse documento!"));
  }

  @Override
  public void resendActivationCode(String email) {
    CustomerDTO customerDTO = this.findByEmail(email);
    Customer customer = customerMapperApp.toDomain(customerDTO);
    customer.canResendActivationCode();
    CustomerDTO validCustomer = customerMapperApp.toDTO(customer);
    String activationCode = activationCodeUseCaseImpl.generate(validCustomer);
    sendActivationCode(customer, activationCode);
  }

  @Override
  public CustomerDTO getById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Não foi encontrado um cliente com o identificador %d!", id)));
  }

  @Override
  public CustomerDTO getByDocument(String documentNumber, DocumentTypeEnum documentType) {
    return repository.findByDocument(documentNumber, documentType).orElseThrow(() -> new NotFoundException(String.format("Não foi encontrado um cliente com o documento %s %s!", documentType, documentNumber)));
  }

  private void validateEmailUsage(boolean isEmailUsed) {
    if (isEmailUsed) {
      ErrorDetail errorDetail = new ErrorDetail("person.email",
              "O email já foi utilizado por outro cadastro!");
      throw new DomainException(errorDetail);
    }
  }

  private void validateDocumentNumberUsage(boolean isDocumentUsed) {
    if (isDocumentUsed) {
      ErrorDetail errorDetail = new ErrorDetail("person.document",
              "O documento já foi utilizado por outro cadastro!");
      throw new DomainException(errorDetail);
    }
  }

  public void sendActivationCode(Customer customer, String activationCode) {
    String verificationMessage = String.format(
            """
            E ai, %s!
            
            Pronto(a) para ter acesso a promoções e descontos exclusivos com a gente?
            
            Para isso, precisamos que ative seu cadastro clicando aqui: %s
            """,
            customer.getFirstName(), activationCode
    );
    emailSender.send(verificationMessage, customer.getEmail().getValue(), "Conclua seu cadastro!");
  }
}
