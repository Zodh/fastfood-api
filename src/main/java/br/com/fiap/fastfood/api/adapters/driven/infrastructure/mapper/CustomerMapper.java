package br.com.fiap.fastfood.api.core.application.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CustomerEntity;
import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.Document;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.Email;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.PhoneNumber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CustomerMapper {

  @Mapping(source = "dto.email", target = "email", qualifiedByName = "mapEmail")
  @Mapping(source = "dto.documentNumber", target = "document", qualifiedByName = "mapDocument")
  @Mapping(source = "dto.phoneNumber", target = "phoneNumber", qualifiedByName = "mapPhoneNumber")
  Customer toDomain(CustomerDTO dto);

  @Mapping(source = "customer.document.value", target = "documentNumber")
  @Mapping(source = "customer.document.type", target = "documentType")
  @Mapping(source = "customer.email.value", target = "email")
  @Mapping(source = "customer.phoneNumber.value", target = "phoneNumber")
  CustomerEntity toEntity(Customer customer);

  @Mapping(source = "entity.email", target = "email", qualifiedByName = "mapEmail")
  @Mapping(source = "entity.documentNumber", target = "document", qualifiedByName = "mapDocument")
  @Mapping(source = "entity.phoneNumber", target = "phoneNumber", qualifiedByName = "mapPhoneNumber")
  Customer toDomain(CustomerEntity entity);

  @Named("mapEmail")
  default Email mapEmail(String email) {
    return new Email(email);
  }

  @Named("mapDocument")
  default Document mapDocument(String documentNumber) {
    return new Document(documentNumber);
  }

  @Named("mapPhoneNumber")
  default PhoneNumber mapPhoneNumber(String phoneNumber) {
    return new PhoneNumber(phoneNumber);
  }

  @Mapping(source = "customer.document.value", target = "documentNumber")
  @Mapping(source = "customer.document.type", target = "documentType")
  @Mapping(source = "customer.phoneNumber.value", target = "phoneNumber")
  @Mapping(source = "customer.email.value", target = "email")
  CustomerDTO toDto(Customer customer);

}
