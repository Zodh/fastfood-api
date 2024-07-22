package br.com.fiap.fastfood.api.adapters.driver.mapper;

import br.com.fiap.fastfood.api.adapters.driver.dto.CustomerDTO;
import br.com.fiap.fastfood.api.core.domain.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CustomerMapper {

  Customer toDomain(CustomerDTO customer);

}
