package br.com.fiap.fastfood.api.adapters.gateway;

import br.com.fiap.fastfood.api.infrastructure.dao.entity.person.CustomerEntity;
import br.com.fiap.fastfood.api.adapters.mapper.CustomerMapperInfra;
import br.com.fiap.fastfood.api.core.application.dto.customer.DocumentTypeEnum;
import br.com.fiap.fastfood.api.infrastructure.dao.repository.person.CustomerRepository;
import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.application.gateway.repository.CustomerRepositoryGateway;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerRepositoryAdapterImpl implements CustomerRepositoryGateway {

  private final CustomerRepository repository;
  private final CustomerMapperInfra mapper;

  @Autowired
  public CustomerRepositoryAdapterImpl(CustomerRepository repository,
      CustomerMapperInfra mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Optional<CustomerDTO> findById(Long identifier) {
    Optional<CustomerEntity> entityOpt = repository.findById(identifier);
    return entityOpt.map(mapper::toDTO);
  }

  @Override
  public CustomerDTO save(CustomerDTO data) {
    CustomerEntity entity = mapper.toEntity(data);
    return mapper.toDTO(repository.save(entity));
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
  public Optional<CustomerDTO> findByEmail(String email) {
    Optional<CustomerEntity> entityOpt = repository.findByEmail(email);
    return entityOpt.map(mapper::toDTO);
  }

  @Override
  public Optional<CustomerDTO> findByDocument(String documentNumber, DocumentTypeEnum documentType) {
    Optional<CustomerEntity> entityOpt = repository.findByDocument(documentNumber, documentType);
    return entityOpt.map(mapper::toDTO);
  }

  @Override
  public void activate(Long identifier) {
    repository.activate(identifier);
  }

}
