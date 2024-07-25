package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.PersonValidator;
import br.com.fiap.fastfood.api.core.domain.ports.outbound.EmailSenderPort;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.CustomerRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.service.ActivationCodeService;
import java.util.List;
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

  public void register() {
    List<ErrorDetail> errorDetails = PersonValidator.validate(this.customer);
    if (!errorDetails.isEmpty()) {
      throw new DomainException(errorDetails);
    }
    customer.setActive(false);
    Customer persistedCustomer = customerRepository.save(customer);
    sendVerificationLink(persistedCustomer);
  }

  private void sendVerificationLink(Customer customer) {
    String activationCode = activationCodeService.generate(customer);
    String verificationMessage = String.format(
        """
        E aiii, %s!
        
        Pronto para ter acesso a promoções e descontos exclusivos com a gente?
        
        Para isso, precisamos que ative seu cadastro clicando aqui: %s
        """,
        customer.getFirstName(), activationCode
    );
    emailSender.send(verificationMessage, customer.getEmail().getValue(), "Conclua seu cadastro!");
  }

}
