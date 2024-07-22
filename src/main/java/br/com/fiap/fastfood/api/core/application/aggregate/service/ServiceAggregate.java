package br.com.fiap.fastfood.api.core.application.aggregate.service;

import br.com.fiap.fastfood.api.core.domain.entity.Customer;
import br.com.fiap.fastfood.api.core.domain.repository.CustomerRepositoryOutboundPort;
import org.springframework.stereotype.Service;

@Service
public class ServiceAggregate implements ServiceAggregateInboundPort {

  private CustomerRepositoryOutboundPort customerRepositoryOutboundPort;

  @Override
  public void register(Customer customer) {
    // ...
    customerRepositoryOutboundPort.register(customer);

    // enviar e-mail de ativacao de cliente.
  }

  @Override
  public void activate(Long customerId) {

  }

}
