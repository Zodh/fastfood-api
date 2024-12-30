package br.com.fiap.fastfood.api.application.gateway.mapper;

import br.com.fiap.fastfood.api.application.dto.customer.CustomerIdentityDTO;
import br.com.fiap.fastfood.api.entities.person.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CustomerIdentityMapperApp {


  @Mapping(source = "customer.document.value", target = "documentNumber")
  CustomerIdentityDTO toIdentityDTO(Customer customer);

}
