package br.com.fiap.fastfood.api.adapters.mapper;

import br.com.fiap.fastfood.api.infrastructure.dao.entity.person.CustomerEntity;
import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CustomerMapperInfra {

  CustomerEntity toEntity(CustomerDTO dto);

  CustomerDTO toDTO(CustomerEntity entity);

}