package br.com.fiap.fastfood.api.core.domain.aggregate;

import static java.util.Objects.isNull;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderInCreationState;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.PersonValidator;
import br.com.fiap.fastfood.api.core.domain.port.outbound.EmailSenderPort;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ServiceAggregate {

  private Customer root;
  private EmailSenderPort emailSender;
  private PersonValidator personValidator;

  public ServiceAggregate(Customer root, EmailSenderPort emailSender,
      PersonValidator personValidator) {
    this.root = root;
    this.emailSender = emailSender;
    this.personValidator = personValidator;
  }

  public ServiceAggregate(Customer root, EmailSenderPort emailSender) {
    this.root = root;
    this.emailSender = emailSender;
  }

  public ServiceAggregate(Customer root) {
    this.root = root;
  }

  public void register(boolean isEmailUsed, boolean isDocumentUsed) {
    List<ErrorDetail> errorDetails = personValidator.validate(this.root);
    if (!errorDetails.isEmpty()) {
      throw new DomainException(errorDetails);
    }
    validateEmailUsage(isEmailUsed);
    validateDocumentNumberUsage(isDocumentUsed);
    root.setActive(false);
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

  public void canResendActivationCode() {
    if (isNull(root) || root.isActive()) {
      throw new DomainException(new ErrorDetail("customer.active",
          "Não é possível solicitar um código de ativação para um cliente ativo!"));
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

  public void checkDocument() {
    if (!this.root.getDocument().isValid()) {
      ErrorDetail errorDetail = new ErrorDetail("document", "O documento não é válido!");
      throw new DomainException(errorDetail);
    }

  }

  public Order createOrder(Collaborator collaborator) {
    Order order = new Order();
    order.changeState(new OrderInCreationState(order));
    order.getState().setCollaborator(collaborator);
    order.getState().setCustomer(this.root);
    order.calculatePrice();
    order.setCreatedAt(LocalDateTime.now());
    return order;
  }

}
