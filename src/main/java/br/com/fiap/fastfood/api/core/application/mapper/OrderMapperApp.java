package br.com.fiap.fastfood.api.core.application.mapper;

import br.com.fiap.fastfood.api.core.application.dto.collaborator.CollaboratorDTO;
import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl.InvoicePendingState;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.order.OrderStateEnum;
import br.com.fiap.fastfood.api.core.domain.model.order.state.OrderState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderAwaitingPaymentState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderCancelledState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderFinishedState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderInCreationState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderInPreparationState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderOperationNotAllowedException;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderReadyState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.impl.OrderReceivedState;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrderMapperApp {

  @Mapping(target = "state", ignore = true)
  @Mapping(source = "dto.products", target = "products", qualifiedByName = "mapProducts")
  @Mapping(source = "dto.customer", target = "customer", qualifiedByName = "mapCustomer")
  @Mapping(source = "dto.collaborator", target = "collaborator", qualifiedByName = "mapCollaborator")
  @Mapping(source = "dto.invoices", target = "invoices", qualifiedByName = "mapInvoices")
  Order toDomain(OrderDTO dto);

  @Named("mapInvoices")
  default List<Invoice> mapInvoices(List<InvoiceDTO> invoiceDTOS) {
    InvoiceMapperApp invoiceMapperApp = new InvoiceMapperAppImpl();
    return Optional.ofNullable(invoiceDTOS).orElse(new ArrayList<>()).stream().map(
        idto -> {
          Invoice i = invoiceMapperApp.toDomain(idto);
          invoiceMapperApp.mapStateImpl(idto.getState(), i);
          return i;
        }).collect(Collectors.toList());
  }

  @Named("mapCollaborator")
  default Collaborator mapCollaborator(CollaboratorDTO collaboratorDTO) {
    return new CollaboratorMapperAppImpl().toDomain(collaboratorDTO);
  }

  @Named("mapCustomer")
  default Customer mapCustomer(CustomerDTO dto) {
    CustomerMapperApp customerMapperApp = new CustomerMapperAppImpl();
    return customerMapperApp.toDomain(dto);
  }

  @Named("mapProducts")
  default List<OrderProduct> mapProducts(List<OrderProductDTO> productDTOS) {
    OrderProductMapperApp orderProductMapperApp = new OrderProductMapperAppImpl();
    return Optional.ofNullable(productDTOS).orElse(new ArrayList<>()).stream().map(
        orderProductMapperApp::toDomain).collect(Collectors.toList());
  }

  @Mapping(source = "order.state", target = "state", qualifiedByName = "mapState")
  @Mapping(source = "order.products", target = "products", qualifiedByName = "mapProductsToDto")
  @Mapping(source = "order.collaborator", target = "collaborator", qualifiedByName = "mapCollaboratorToDto")
  @Mapping(source = "order.customer", target = "customer", qualifiedByName = "mapCustomerToDto")
  @Mapping(source = "order.invoices", target = "invoices", qualifiedByName = "mapInvoicesToDTO")
  @Mapping(source = "order.invoices", target = "invoice", qualifiedByName = "mapInvoiceToDTO")
  OrderDTO toDTO(Order order);

  @Named("mapInvoicesToDTO")
  default List<InvoiceDTO> mapInvoicesToDTO(List<Invoice> invoices) {
    InvoiceMapperApp invoiceMapperApp = new InvoiceMapperAppImpl();
    return Optional.ofNullable(invoices).orElse(new ArrayList<>()).stream().map(invoiceMapperApp::toDto).collect(Collectors.toList());
  }

  @Named("mapInvoiceToDTO")
  default InvoiceDTO mapInvoiceToDTO(List<Invoice> invoices) {
    InvoiceMapperApp invoiceMapperApp = new InvoiceMapperAppImpl();

    return Optional.ofNullable(invoices).orElse(Collections.emptyList()).stream().filter(i -> Objects.nonNull(i) && i.getState() instanceof InvoicePendingState).map(
        invoiceMapperApp::toDto).findFirst().orElse(null);
  }

  @Named("mapProductsToDto")
  default List<OrderProductDTO> mapProductsToDto(List<OrderProduct> orderProducts) {
    OrderProductMapperApp orderProductMapperApp = new OrderProductMapperAppImpl();
    return Optional.ofNullable(orderProducts).orElse(new ArrayList<>()).stream().map(
        orderProductMapperApp::toDto).collect(
        Collectors.toList());
  }

  @Named("mapCollaboratorToDto")
  default CollaboratorDTO mapCollaboratorToDto(Collaborator collaborator) {
    return new CollaboratorDTO(collaborator.getId(), collaborator.getName(), collaborator.getRole().name());
  }

  @Named("mapCustomerToDto")
  default CustomerDTO mapCustomerToDto(Customer customer) {
    CustomerMapperApp customerMapperApp = new CustomerMapperAppImpl();
    return customerMapperApp.toDTO(customer);
  }

  default OrderState mapStateImpl(OrderStateEnum state, Order order) {
    switch (state) {
      case FINISHED -> {
        return new OrderFinishedState(order);
      }
      case AWAITING_PAYMENT -> {
        return new OrderAwaitingPaymentState(order);
      }
      case RECEIVED -> {
        return new OrderReceivedState(order);
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
      case READY -> {
        return new OrderReadyState(order);
      }
    }
    throw new OrderOperationNotAllowedException("mapStateImpl");
  }

  @Named("mapState")
  default OrderStateEnum mapState(OrderState orderState) {
    if (orderState instanceof OrderAwaitingPaymentState) {
      return OrderStateEnum.AWAITING_PAYMENT;
    }
    if (orderState instanceof OrderReceivedState) {
      return OrderStateEnum.RECEIVED;
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
    if (orderState instanceof OrderReadyState) {
      return OrderStateEnum.READY;
    }
    throw new OrderOperationNotAllowedException("mapState");
  }

}
