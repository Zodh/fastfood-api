package br.com.fiap.fastfood.api.core.domain.aggregate;

import static java.util.Objects.isNull;

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

  public ServiceAggregate(String email, CustomerRepositoryPort customerRepository, ActivationCodeService activationCodeService, EmailSenderPort emailSender) {
    this.customer = customerRepository.findByEmail(email).orElseThrow(() -> new DomainException(new ErrorDetail("customer", "Não foi encontrado um cliente com o e-mail informado!")));
    this.activationCodeService = activationCodeService;
    this.emailSender = emailSender;
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

  public void resendVerificationLink() {
    if (isNull(customer) || customer.isActive()) {
      throw new DomainException(new ErrorDetail("customer.active", "Não é possível solicitar um código de ativação para um cliente ativo!"));
    }
    sendVerificationLink(customer);
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

}
