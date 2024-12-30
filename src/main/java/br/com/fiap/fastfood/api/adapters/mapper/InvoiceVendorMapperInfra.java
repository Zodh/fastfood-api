package br.com.fiap.fastfood.api.adapters.mapper;

import br.com.fiap.fastfood.api.application.dto.invoice.InvoiceVendorDTO;
import br.com.fiap.fastfood.api.infrastructure.dao.entity.invoice.InvoiceVendorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface InvoiceVendorMapperInfra {

    InvoiceVendorEntity toEntity(InvoiceVendorDTO dto);
    InvoiceVendorDTO toDTO(InvoiceVendorEntity entity);

}
