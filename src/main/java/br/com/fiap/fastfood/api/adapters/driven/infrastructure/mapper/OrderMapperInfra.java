package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order.OrderEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CollaboratorEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CustomerEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.OrderProductEntity;
import br.com.fiap.fastfood.api.core.application.dto.collaborator.CollaboratorDTO;
import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.OrderProductDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrderMapperInfra {

  @Mapping(source = "dto.products", target = "products", qualifiedByName = "mapProductsToEntity")
  @Mapping(source = "dto.collaborator", target = "collaborator", qualifiedByName = "mapCollaboratorToEntity")
  @Mapping(source = "dto.customer", target = "customer", qualifiedByName = "mapCustomerToEntity")
  OrderEntity toEntity(OrderDTO dto);

  @Mapping(source = "orderEntity.products", target = "products", qualifiedByName = "mapProductsToDTO")
  @Mapping(source = "orderEntity.collaborator", target = "collaborator", qualifiedByName = "mapCollaboratorToDTO")
  @Mapping(source = "orderEntity.customer", target = "customer", qualifiedByName = "mapCustomerToDTO")
  OrderDTO toDTO(OrderEntity orderEntity);

  @Named("mapCollaboratorToDTO")
  default CollaboratorDTO mapCollaboratorToDTO(CollaboratorEntity collaboratorEntity) {
    CollaboratorMapperInfra collaboratorMapperInfra = new CollaboratorMapperInfraImpl();
    return collaboratorMapperInfra.toDTO(collaboratorEntity);
  }

  @Named("mapCustomerToDTO")
  default CustomerDTO mapCustomerToDTO(CustomerEntity customer) {
    CustomerMapperInfra customerMapperInfra = new CustomerMapperInfraImpl();
    return customerMapperInfra.toDTO(customer);
  }

  @Named("mapProductsToDTO")
  default List<OrderProductDTO> mapProductsToDTO(List<OrderProductEntity> products) {
    OrderProductMapperInfra orderProductMapperInfra = new OrderProductMapperInfraImpl();
    return Optional.ofNullable(products).orElse(new ArrayList<>()).stream()
        .map(orderProductMapperInfra::toDTO)
        .collect(Collectors.toList());
  }

  @Named("mapCollaboratorToEntity")
  default CollaboratorEntity mapCollaboratorToEntity(CollaboratorDTO dto) {
    return new CollaboratorMapperInfraImpl().toEntity(dto);
  }

  @Named("mapCustomerToEntity")
  default CustomerEntity mapCustomerToEntity(CustomerDTO dto) {
    return new CustomerMapperInfraImpl().toEntity(dto);
  }

  @Named("mapProductsToEntity")
  default List<OrderProductEntity> mapProductsToEntity(List<OrderProductDTO> products) {
    OrderProductMapperInfra mapper = new OrderProductMapperInfraImpl();
    return Optional.ofNullable(products).orElse(new ArrayList<>()).stream().map(mapper::toEntity).collect(
        Collectors.toList());
  }

}
