package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driver.dto.customer.CustomerIdentityDTO;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CustomerIdentityMapper {


  @Mapping(source = "customer.document.value", target = "documentNumber")
  CustomerIdentityDTO toIdentityDTO(Customer customer);

}
