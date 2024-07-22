package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CustomerEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CustomerMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.person.CustomerRepository;
import br.com.fiap.fastfood.api.core.domain.entity.Customer;
import br.com.fiap.fastfood.api.core.domain.repository.CustomerRepositoryOutboundPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerRepositoryAdapterImpl implements CustomerRepositoryOutboundPort {

  private final CustomerRepository customerRepository;
  private final CustomerMapper mapper;

  @Autowired
  public CustomerRepositoryAdapterImpl(CustomerRepository customerRepository,
      CustomerMapper mapper) {
    this.customerRepository = customerRepository;
    this.mapper = mapper;
  }

  @Override
  public void register(Customer customer) {
    CustomerEntity entity = mapper.toEntity(customer);
    customerRepository.save(entity);
  }
}
