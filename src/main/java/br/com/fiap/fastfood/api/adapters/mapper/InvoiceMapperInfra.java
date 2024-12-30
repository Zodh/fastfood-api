package br.com.fiap.fastfood.api.adapters.mapper;

import br.com.fiap.fastfood.api.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.application.dto.invoice.InvoiceVendorDTO;
import br.com.fiap.fastfood.api.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.infrastructure.dao.entity.invoice.InvoiceEntity;
import br.com.fiap.fastfood.api.infrastructure.dao.entity.invoice.InvoiceVendorEntity;
import br.com.fiap.fastfood.api.infrastructure.dao.entity.order.OrderEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface InvoiceMapperInfra {

    @Mapping(source = "dto.order", target = "order", qualifiedByName = "mapOrderToEntity")
    InvoiceEntity toEntity(InvoiceDTO dto);

    @Mapping(source = "entity.order", target = "order", qualifiedByName = "mapOrderToDTO")
    InvoiceDTO toDTO(InvoiceEntity entity);

    List<InvoiceDTO> toDTO(List<InvoiceEntity> entity);

    @Named("mapOrderToEntity")
    default OrderEntity mapOrderToEntity(OrderDTO orderDTO) {
        return new OrderMapperInfraImpl().toEntity(orderDTO);
    }

    @Named("mapVendorToDomain")
    default InvoiceVendorDTO mapVendorToDomain(InvoiceVendorEntity entity) {
        InvoiceVendorMapperInfra invoiceMapper = new InvoiceVendorMapperInfraImpl();
        return invoiceMapper.toDTO(entity);
    }

    @Named("mapOrderToDTO")
    default OrderDTO mapOrderToDTO(OrderEntity entity) {
        OrderMapperInfra orderMapperInfra = new OrderMapperInfraImpl();
        return orderMapperInfra.toDTO(entity);
    }
}
