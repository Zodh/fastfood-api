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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrderMapper {

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
    CollaboratorMapper collaboratorMapper = new CollaboratorMapperImpl();
    return collaboratorMapper.toDTO(collaboratorEntity);
  }

  @Named("mapCustomerToDTO")
  default CustomerDTO mapCustomerToDTO(CustomerEntity customer) {
    CustomerMapper customerMapper = new CustomerMapperImpl();
    return customerMapper.toDTO(customer);
  }

  @Named("mapProductsToDTO")
  default List<OrderProductDTO> mapProductsToDTO(List<OrderProductEntity> products) {
    OrderProductMapper orderProductMapper = new OrderProductMapperImpl();
    return Optional.ofNullable(products).orElse(new ArrayList<>()).stream()
        .map(orderProductMapper::toDTO)
        .collect(Collectors.toList());
  }

  @Named("mapCollaboratorToEntity")
  default CollaboratorEntity mapCollaboratorToEntity(CollaboratorDTO dto) {
    return new CollaboratorMapperImpl().toEntity(dto);
  }

  @Named("mapCustomerToEntity")
  default CustomerEntity mapCustomerToEntity(CustomerDTO dto) {
    return new CustomerMapperImpl().toEntity(dto);
  }

  @Named("mapProductsToEntity")
  default List<OrderProductEntity> mapProductsToEntity(List<OrderProductDTO> products) {
    OrderProductMapper mapper = new OrderProductMapperImpl();
    return Optional.ofNullable(products).orElse(new ArrayList<>()).stream().map(mapper::toEntity).collect(
        Collectors.toList());
  }

}
