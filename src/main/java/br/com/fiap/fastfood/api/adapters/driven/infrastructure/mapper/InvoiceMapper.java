package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceStatus;
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

    @Mapping(target = "state", ignore = true)
    List<Invoice> toDomain(List<InvoiceEntity> entity);

    default InvoiceState mapStateImpl(InvoiceStatus state, Invoice invoice) {
        switch (state) {
            case EXPIRED -> {
                return new InvoiceExpiredState(invoice);
            }
            case PENDING -> {
                return new InvoicePendingState(invoice);
            }
            case PAID -> {
                return new InvoicePaidState(invoice);
            }
            case CANCELLED -> {
                return new InvoiceCancelledState(invoice);
            }
        }
        throw new InvoiceStateException("A operação não pode ser executada no status atual do pedido!");
    }

    @Named("mapState")
    default InvoiceStatus mapState(InvoiceState invoiceState) {
        if (invoiceState instanceof InvoicePendingState) {
            return InvoiceStatus.PENDING;
        }
        if (invoiceState instanceof InvoiceExpiredState) {
            return InvoiceStatus.EXPIRED;
        }
        if (invoiceState instanceof InvoicePaidState) {
            return InvoiceStatus.PAID;
        }
        if (invoiceState instanceof InvoiceCancelledState) {
            return InvoiceStatus.CANCELLED;
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
