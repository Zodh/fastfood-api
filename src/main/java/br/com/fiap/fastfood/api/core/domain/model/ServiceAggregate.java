package br.com.fiap.fastfood.api.core.domain.model;


import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.PersonValidator;
import br.com.fiap.fastfood.api.core.domain.ports.EmailSenderOutboundPort;
import br.com.fiap.fastfood.api.core.domain.repository.CustomerRepositoryOutboundPort;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceAggregate {

  private Customer customer;
  private CustomerRepositoryOutboundPort customerRepository;
  private EmailSenderOutboundPort emailSender;

  public void register() {
    List<ErrorDetail> errorDetails = PersonValidator.validate(this.customer);
    if (!errorDetails.isEmpty()) {
      throw new DomainException(errorDetails);
    }
    customer.setActive(false);
    customerRepository.save(customer);
    emailSender.send("Ative sua conta clicando no link a seguir: link-aqui", customer.getEmail().getValue(), "Conclua seu cadastro!");
  }

}
