package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CustomerEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CustomerMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.person.CustomerRepository;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
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
  public Customer findById(Long identifier) {
    return null;
  }

  @Override
  public Customer save(Customer data) {
    CustomerEntity entity = mapper.toEntity(data);
    return mapper.toDomain(customerRepository.save(entity));
  }
}
