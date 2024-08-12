package br.com.fiap.fastfood.api.core.application.mapper;

import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.domain.exception.InvoiceStateException;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceState;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceStateEnum;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl.InvoiceCancelledState;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl.InvoiceExpiredState;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl.InvoicePaidState;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl.InvoicePendingState;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface InvoiceMapperApp {

  @Named("mapState")
  default InvoiceStateEnum mapState(InvoiceState invoiceState) {
    if (invoiceState instanceof InvoicePendingState) {
      return InvoiceStateEnum.PENDING;
    }
    if (invoiceState instanceof InvoiceExpiredState) {
      return InvoiceStateEnum.EXPIRED;
    }
    if (invoiceState instanceof InvoicePaidState) {
      return InvoiceStateEnum.PAID;
    }
    if (invoiceState instanceof InvoiceCancelledState) {
      return InvoiceStateEnum.CANCELLED;
    }
    throw new InvoiceStateException("A operação não pode ser executada no status atual do pedido!");
  }

  default void mapStateImpl(InvoiceStateEnum state, Invoice invoice) {
    switch (state) {
      case EXPIRED -> {
        invoice.setState(new InvoiceExpiredState(invoice));
        return;
      }
      case PENDING -> {
        invoice.setState(new InvoicePendingState(invoice));
        return;
      }
      case PAID -> {
        invoice.setState(new InvoicePaidState(invoice));
        return;
      }
      case CANCELLED -> {
        invoice.setState(new InvoiceCancelledState(invoice));
        return;
      }
    }
    throw new InvoiceStateException("A operação não pode ser executada no status atual do pedido!");
  }

  @Mapping(source = "invoice.state", target = "state", qualifiedByName = "mapState")
  @Mapping(source = "invoice.order", target = "order", qualifiedByName = "mapOrderToDTO")
  InvoiceDTO toDto(Invoice invoice);

  @Named("mapOrderToDTO")
  default OrderDTO mapOrderToDTO(Order order) {
    return new OrderMapperAppImpl().toDTO(order);
  }

  @Mapping(source = "dto.order", target = "order", qualifiedByName = "mapOrder")
  @Mapping(target = "state", ignore = true)
  Invoice toDomain(InvoiceDTO dto);

  @Named("mapOrder")
  default Order mapOrder(OrderDTO orderDTO) {
    OrderMapperApp orderMapperApp = new OrderMapperAppImpl();
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));
    return order;
  }

}
