package br.com.fiap.fastfood.api.application.usecase;

import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.dto.customer.DocumentTypeEnum;
import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.mapper.CustomerMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.CustomerMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.CustomerServicePort;
import br.com.fiap.fastfood.api.core.application.port.outbound.ActivationCodeLinkGeneratorPort;
import br.com.fiap.fastfood.api.core.application.port.repository.ActivationCodeRepositoryPort;
import br.com.fiap.fastfood.api.core.application.port.repository.CustomerRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.port.outbound.EmailSenderPort;
import br.com.fiap.fastfood.api.entities.person.Customer;
import br.com.fiap.fastfood.api.entities.person.PersonValidator;
import br.com.fiap.fastfood.api.entities.person.vo.Document;

import java.util.List;
import java.util.UUID;

public class CustomerUseCase implements CustomerServicePort {

  private final CustomerRepositoryPort repository;
  private final EmailSenderPort emailSender;
  private final ActivationCodeUseCase activationCodeUseCase;
  private final CustomerMapperApp customerMapperApp;

  public CustomerUseCase(
      CustomerRepositoryPort repository,
      EmailSenderPort emailSender,
      ActivationCodeRepositoryPort activationCodeRepository,
      ActivationCodeLinkGeneratorPort activationCodeLinkGeneratorPort
  ) {
    this.repository = repository;
    this.emailSender = emailSender;
    this.activationCodeUseCase = new ActivationCodeUseCase(activationCodeRepository,
        activationCodeLinkGeneratorPort,
        repository);
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

    List<ErrorDetail> errorDetails = new PersonValidator().validate(customer);
    if (!errorDetails.isEmpty()) {
      throw new DomainException(errorDetails);
    }

    this.validateEmailUsage(isEmailUsed);
    this.validateDocumentNumberUsage(isDocumentUsed);
    customer.setActive(false);

    CustomerDTO registeredCustomer = customerMapperApp.toDTO(customer);
    CustomerDTO persistedCustomer = repository.save(registeredCustomer);
    String activationCode = activationCodeUseCase.generate(persistedCustomer);
    Customer result = customerMapperApp.toDomain(persistedCustomer);

    sendActivationCode(result, activationCode);
  }

  @Override
  public void activate(UUID code) {
    activationCodeUseCase.activate(code);
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
    String activationCode = activationCodeUseCase.generate(validCustomer);
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
