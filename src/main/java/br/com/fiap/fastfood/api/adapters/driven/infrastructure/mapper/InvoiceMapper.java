package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceVendorEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order.OrderEntity;
import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceVendorDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface InvoiceMapper {

    @Mapping(source = "dto.order", target = "order", qualifiedByName = "mapOrderToEntity")
    InvoiceEntity toEntity(InvoiceDTO dto);

    @Mapping(source = "entity.order", target = "order", qualifiedByName = "mapOrderToDTO")
    InvoiceDTO toDTO(InvoiceEntity entity);

    List<InvoiceDTO> toDTO(List<InvoiceEntity> entity);

    @Named("mapOrderToEntity")
    default OrderEntity mapOrderToEntity(OrderDTO orderDTO) {
        return new OrderMapperImpl().toEntity(orderDTO);
    }

    @Named("mapVendorToDomain")
    default InvoiceVendorDTO mapVendorToDomain(InvoiceVendorEntity entity) {
        InvoiceVendorMapper invoiceMapper = new InvoiceVendorMapperImpl();
        return invoiceMapper.toDTO(entity);
    }

    @Named("mapOrderToDTO")
    default OrderDTO mapOrderToDTO(OrderEntity entity) {
        OrderMapper orderMapper = new OrderMapperImpl();
        return orderMapper.toDTO(entity);
    }
}
