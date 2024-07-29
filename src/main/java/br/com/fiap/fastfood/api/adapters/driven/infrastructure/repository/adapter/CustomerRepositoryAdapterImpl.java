package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CustomerEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.DocumentTypeEnum;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CustomerMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.person.CustomerRepository;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.Document;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.CustomerRepositoryPort;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerRepositoryAdapterImpl implements CustomerRepositoryPort {

  private final CustomerRepository repository;
  private final CustomerMapper mapper;

  @Autowired
  public CustomerRepositoryAdapterImpl(CustomerRepository repository,
      CustomerMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Optional<Customer> findById(Long identifier) {
    Optional<CustomerEntity> entityOpt = repository.findById(identifier);
    return entityOpt.map(mapper::toDomain);
  }

  @Override
  public Customer save(Customer data) {
    CustomerEntity entity = mapper.toEntity(data);
    return mapper.toDomain(repository.save(entity));
  }

  @Override
  public boolean delete(Long identifier) {
    Optional<CustomerEntity> customerEntityOpt = repository.findById(identifier);
    if (customerEntityOpt.isEmpty()) {
      return false;
    }
    repository.deleteById(identifier);
    return true;
  }

  @Override
  public Optional<Customer> findByEmail(String email) {
    Optional<CustomerEntity> entityOpt = repository.findByEmail(email);
    return entityOpt.map(mapper::toDomain);
  }

  @Override
  public Optional<Customer> findByDocument(Document document) {
    Optional<CustomerEntity> entityOpt = repository.findByDocument(document.getValue(),
        DocumentTypeEnum.valueOf(document.getType().name()));
    return entityOpt.map(mapper::toDomain);
  }

  @Override
  public void activate(Long identifier) {
    repository.activate(identifier);
  }

}
