package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order.OrderEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CollaboratorEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CustomerEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.OrderProductEntity;
import br.com.fiap.fastfood.api.adapters.driver.dto.collaborator.CollaboratorDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.order.OrderStateEnum;
import br.com.fiap.fastfood.api.core.domain.model.order.state.OrderState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderAwaitingPaymentState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderAwaitingPreparationState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderCancelledState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderFinishedState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderInCreationState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderInPreparationState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderOperationNotAllowedException;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderPickupReadyState;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
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

  @Mapping(source = "order.state", target = "state", qualifiedByName = "mapState")
  @Mapping(source = "order.products", target = "products", qualifiedByName = "mapProductsToEntity")
  @Mapping(source = "order.collaborator", target = "collaborator", qualifiedByName = "mapCollaboratorToEntity")
  @Mapping(source = "order.customer", target = "customer", qualifiedByName = "mapCustomerToEntity")
  OrderEntity toEntity(Order order);

  @Mapping(target = "state", ignore = true)
  @Mapping(source = "orderEntity.products", target = "products", qualifiedByName = "mapProductsToDomain")
  @Mapping(source = "orderEntity.collaborator", target = "collaborator", qualifiedByName = "mapCollaboratorToDomain")
  @Mapping(source = "orderEntity.customer", target = "customer", qualifiedByName = "mapCustomerToDomain")
  Order toDomain(OrderEntity orderEntity);

  @Mapping(source = "order.state", target = "state", qualifiedByName = "mapState")
  @Mapping(source = "order.products", target = "products", qualifiedByName = "mapProductsToDto")
  @Mapping(source = "order.collaborator", target = "collaborator", qualifiedByName = "mapCollaboratorToDto")
  @Mapping(source = "order.customer", target = "customer", qualifiedByName = "mapCustomerToDto")
  OrderDTO toDto(Order order);

  @Named("mapProductsToDto")
  default List<OrderProductDTO> mapProductsToDto(List<OrderProduct> orderProducts) {
    OrderProductMapper orderProductMapper = new OrderProductMapperImpl();
    return Optional.ofNullable(orderProducts).orElse(Collections.emptyList()).stream().map(orderProductMapper::toDto).collect(Collectors.toList());
  }

  @Named("mapCollaboratorToDto")
  default CollaboratorDTO mapCollaboratorToDto(Collaborator collaborator) {
    return new CollaboratorDTO(collaborator.getId(), collaborator.getName(), collaborator.getRole());
  }

  @Named("mapCustomerToDto")
  default CustomerDTO mapCustomerToDto(Customer customer) {
    CustomerMapper customerMapper = new CustomerMapperImpl();
    return customerMapper.toDto(customer);
  }

  @Named("mapCollaboratorToDomain")
  default Collaborator mapCollaboratorToDomain(CollaboratorEntity collaboratorEntity) {
    CollaboratorMapper collaboratorMapper = new CollaboratorMapperImpl();
    return collaboratorMapper.toDomain(collaboratorEntity);
  }

  @Named("mapCustomerToDomain")
  default Customer mapCustomerToDomain(CustomerEntity customer) {
    CustomerMapper customerMapper = new CustomerMapperImpl();
    return customerMapper.toDomain(customer);
  }

  @Named("mapProductsToDomain")
  default List<OrderProduct> mapProductsToDomain(List<OrderProductEntity> products) {
    OrderProductMapper orderProductMapper = new OrderProductMapperImpl();
    return Optional.ofNullable(products).orElse(Collections.emptyList()).stream()
        .map(orderProductMapper::toDomain)
        .collect(Collectors.toList());
  }

  default OrderState mapStateImpl(OrderStateEnum state, Order order) {
    switch (state) {
      case FINISHED -> {
        return new OrderFinishedState(order);
      }
      case AWAITING_PAYMENT -> {
        return new OrderAwaitingPaymentState(order);
      }
      case AWAITING_PREPARATION -> {
        return new OrderAwaitingPreparationState(order);
      }
      case CANCELLED -> {
        return new OrderCancelledState(order);
      }
      case IN_CREATION -> {
        return new OrderInCreationState(order);
      }
      case IN_PREPARATION -> {
        return new OrderInPreparationState(order);
      }
      case PICKUP_READY -> {
        return new OrderPickupReadyState(order);
      }
    }
    throw new OrderOperationNotAllowedException("mapStateImpl");
  }

  @Named("mapCollaboratorToEntity")
  default CollaboratorEntity mapCollaboratorToEntity(Collaborator collaborator) {
    return new CollaboratorMapperImpl().toEntity(collaborator);
  }

  @Named("mapCustomerToEntity")
  default CustomerEntity mapCustomerToEntity(Customer customer) {
    return new CustomerMapperImpl().toEntity(customer);
  }

  @Named("mapProductsToEntity")
  default List<OrderProductEntity> mapProductsToEntity(List<OrderProduct> products) {
    OrderProductMapper mapper = new OrderProductMapperImpl();
    return Optional.ofNullable(products).orElse(Collections.emptyList()).stream().map(mapper::toEntity).collect(
        Collectors.toList());
  }

  @Named("mapState")
  default OrderStateEnum mapState(OrderState orderState) {
    if (orderState instanceof OrderAwaitingPaymentState) {
      return OrderStateEnum.AWAITING_PAYMENT;
    }
    if (orderState instanceof OrderAwaitingPreparationState) {
      return OrderStateEnum.AWAITING_PREPARATION;
    }
    if (orderState instanceof OrderCancelledState) {
      return OrderStateEnum.CANCELLED;
    }
    if (orderState instanceof OrderFinishedState) {
      return OrderStateEnum.FINISHED;
    }
    if (orderState instanceof OrderInCreationState) {
      return OrderStateEnum.IN_CREATION;
    }
    if (orderState instanceof OrderInPreparationState) {
      return OrderStateEnum.IN_PREPARATION;
    }
    if (orderState instanceof OrderPickupReadyState) {
      return OrderStateEnum.PICKUP_READY;
    }
    throw new OrderOperationNotAllowedException("mapState");
  }

}
