package br.com.fiap.fastfood.api.core.application.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.InvoiceVendorMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapperImpl;
import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceStateEnum;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceVendorEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order.OrderEntity;
import br.com.fiap.fastfood.api.core.domain.exception.InvoiceStateException;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceState;
import br.com.fiap.fastfood.api.core.domain.model.invoice.InvoiceVendor;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl.InvoiceCancelledState;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl.InvoiceExpiredState;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl.InvoicePaidState;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl.InvoicePendingState;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface InvoiceMapper {

    @Mapping(source = "invoice.state", target = "state", qualifiedByName = "mapState")
    @Mapping(source = "invoice.vendor", target = "vendor", qualifiedByName = "mapVendorToEntity")
    @Mapping(source = "invoice.order", target = "order", qualifiedByName = "mapOrderToEntity")
    InvoiceEntity toEntity(Invoice invoice);

    @Mapping(target = "state", ignore = true)
    @Mapping(source = "entity.vendor", target = "vendor", qualifiedByName = "mapVendorToDomain")
    @Mapping(source = "entity.order", target = "order", qualifiedByName = "mapOrderToDomain")
    Invoice toDomain(InvoiceEntity entity);

    @Mapping(source = "invoice.state", target = "state", qualifiedByName = "mapState")
    InvoiceDTO toDto(Invoice invoice);

    @Mapping(target = "state", ignore = true)
    List<Invoice> toDomain(List<InvoiceEntity> entity);

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

    @Named("mapVendorToEntity")
    default InvoiceVendorEntity mapVendorToEntity(InvoiceVendor vendor) {
        return new InvoiceVendorMapperImpl().toEntity(vendor);
    }

    @Named("mapOrderToEntity")
    default OrderEntity mapOrderToEntity(Order order) {
        return new OrderMapperImpl().toEntity(order);
    }

    @Named("mapVendorToDomain")
    default InvoiceVendor mapVendorToDomain(InvoiceVendorEntity entity) {
        InvoiceVendorMapper invoiceMapper = new InvoiceVendorMapperImpl();
        return invoiceMapper.toDomain(entity);
    }

    @Named("mapOrderToDomain")
    default Order mapOrderToDomain(OrderEntity entity) {
        OrderMapper orderMapper = new OrderMapperImpl();
        return orderMapper.toDomain(entity);
    }
}
