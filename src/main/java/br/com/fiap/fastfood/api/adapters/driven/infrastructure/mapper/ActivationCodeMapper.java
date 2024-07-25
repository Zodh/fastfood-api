package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CustomerEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.activation.ActivationCodeEntity;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.activation.ActivationCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ActivationCodeMapper {

  @Mapping(source = "domain.customer", target = "customer", qualifiedByName = "mapCustomerEntity")
  ActivationCodeEntity toEntity(ActivationCode domain);

  @Named("mapCustomerEntity")
  default CustomerEntity mapCustomerEntity(Customer customer) {
    CustomerMapper customerMapper = new CustomerMapperImpl();
    return customerMapper.toEntity(customer);
  }

  @Mapping(source = "entity.customer", target = "customer", qualifiedByName = "mapCustomer")
  ActivationCode toDomain(ActivationCodeEntity entity);

  @Named("mapCustomer")
  default Customer mapCustomer(CustomerEntity entity) {
    CustomerMapper customerMapper = new CustomerMapperImpl();
    return customerMapper.toDomain(entity);
  }

}
