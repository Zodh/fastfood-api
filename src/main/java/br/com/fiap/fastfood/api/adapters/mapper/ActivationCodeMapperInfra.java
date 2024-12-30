package br.com.fiap.fastfood.api.adapters.mapper;

import br.com.fiap.fastfood.api.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.application.dto.customer.activation.ActivationCodeDTO;
import br.com.fiap.fastfood.api.infrastructure.dao.entity.person.CustomerEntity;
import br.com.fiap.fastfood.api.infrastructure.dao.entity.person.activation.ActivationCodeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ActivationCodeMapperInfra {

  @Mapping(source = "dto.customer", target = "customer", qualifiedByName = "mapCustomerEntity")
  ActivationCodeEntity toEntity(ActivationCodeDTO dto);

  @Named("mapCustomerEntity")
  default CustomerEntity mapCustomerEntity(CustomerDTO dto) {
    CustomerMapperInfra customerMapperInfra = new CustomerMapperInfraImpl();
    return customerMapperInfra.toEntity(dto);
  }

  @Mapping(source = "entity.customer", target = "customer", qualifiedByName = "mapCustomerDTO")
  ActivationCodeDTO toDTO(ActivationCodeEntity entity);

  @Named("mapCustomerDTO")
  default CustomerDTO mapCustomerDTO(CustomerEntity entity) {
    CustomerMapperInfra customerMapperInfra = new CustomerMapperInfraImpl();
    return customerMapperInfra.toDTO(entity);
  }

}
