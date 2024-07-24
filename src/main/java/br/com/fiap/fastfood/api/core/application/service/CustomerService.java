package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.ServiceAggregate;
import br.com.fiap.fastfood.api.core.domain.ports.EmailSenderOutboundPort;
import br.com.fiap.fastfood.api.core.domain.repository.CustomerRepositoryOutboundPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
  
  private final CustomerRepositoryOutboundPort customerRepositoryOutboundPort;
  private final EmailSenderOutboundPort emailSenderOutboundPort;

  public void register(Customer customer) {
    ServiceAggregate serviceAggregate = new ServiceAggregate(customer, customerRepositoryOutboundPort, emailSenderOutboundPort);
    serviceAggregate.register();
  }

  public void activate(Long customerId) {

  }

}
