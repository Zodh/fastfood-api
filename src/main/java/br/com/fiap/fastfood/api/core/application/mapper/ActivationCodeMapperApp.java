package br.com.fiap.fastfood.api.core.application.mapper;

import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.dto.customer.activation.ActivationCodeDTO;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.activation.ActivationCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ActivationCodeMapperApp {

  @Mapping(source = "activationCode.customer", target = "customer", qualifiedByName = "mapCustomerToDTO")
  ActivationCodeDTO toDTO(ActivationCode activationCode);

  @Mapping(source = "dto.customer", target = "customer", qualifiedByName = "mapCustomerToDomain")
  ActivationCode toDomain(ActivationCodeDTO dto);

  @Named("mapCustomerToDomain")
  default Customer mapCustomerToDomain(CustomerDTO dto) {
    return new CustomerMapperAppImpl().toDomain(dto);
  }

  @Named("mapCustomerToDTO")
  default CustomerDTO mapCustomerToDTO(Customer customer) {
    CustomerMapperApp customerMapperApp = new CustomerMapperAppImpl();
    return customerMapperApp.toDTO(customer);
  }

}
