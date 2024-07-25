package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.PersonValidator;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.Document;
import br.com.fiap.fastfood.api.core.domain.ports.outbound.EmailSenderPort;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.CustomerRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.service.ActivationCodeService;
import java.util.List;
import java.util.Optional;
import lombok.Data;

@Data
public class ServiceAggregate {

  private Customer customer;

  private CustomerRepositoryPort customerRepository;
  private EmailSenderPort emailSender;
  private ActivationCodeService activationCodeService;

  public ServiceAggregate(
      Customer customer,
      CustomerRepositoryPort customerRepository,
      EmailSenderPort emailSender,
      ActivationCodeService activationCodeService
  ) {
    this.customer = customer;
    this.customerRepository = customerRepository;
    this.emailSender = emailSender;
    this.activationCodeService = activationCodeService;
  }

  public ServiceAggregate(
          Document document,
          CustomerRepositoryPort customerRepository
  ) {
    this.customer = new Customer();
    this.customer.setDocument(document);
    this.customerRepository = customerRepository;
  }

  public void register() {
    List<ErrorDetail> errorDetails = PersonValidator.validate(this.customer);
    if (!errorDetails.isEmpty()) {
      throw new DomainException(errorDetails);
    }
    validateEmailUsage();
    validateDocumentNumberUsage();
    customer.setActive(false);
    Customer persistedCustomer = customerRepository.save(customer);
    sendVerificationLink(persistedCustomer);
  }

  private void validateEmailUsage() {
    String email = customer.getEmail().getValue();
    Optional<Customer> existingCustomer = customerRepository.findByEmail(email);
    if (existingCustomer.isPresent()) {
      ErrorDetail errorDetail = new ErrorDetail("person.email", "O email já foi utilizado por outro cadastro!");
      throw new DomainException(errorDetail);
    }
  }

  private void validateDocumentNumberUsage() {
    Document document = customer.getDocument();
    Optional<Customer> existingCustomer = customerRepository.findByDocument(document);
    if (existingCustomer.isPresent()) {
      ErrorDetail errorDetail = new ErrorDetail("person.document", "O documento já foi utilizado por outro cadastro!");
      throw new DomainException(errorDetail);
    }
  }

  private void sendVerificationLink(Customer customer) {
    String activationCode = activationCodeService.generate(customer);
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

  public Customer identify() {
    if (!this.customer.getDocument().isValid()) {
      ErrorDetail errorDetail = new ErrorDetail("person.document", "O documento não é válido!");
      throw new DomainException(errorDetail);
    }

    Optional<Customer> identifiedCustomer = customerRepository.findByDocument(this.customer.getDocument());

    if (identifiedCustomer.isEmpty()) {
      ErrorDetail errorDetail = new ErrorDetail("person", "Não existe um cliente cadastrado com esse documento!");
      throw new DomainException(errorDetail);
    }

    return identifiedCustomer.get();

  }
}
