package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.InvoiceMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.invoice.InvoiceRepository;
import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.application.port.repository.InvoiceRepositoryPort;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InvoiceRepositoryAdapterImpl implements InvoiceRepositoryPort {

    private final InvoiceRepository repository;
    private final InvoiceMapper mapper;

    @Autowired
    public InvoiceRepositoryAdapterImpl(InvoiceRepository repository, InvoiceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<InvoiceDTO> findById(Long identifier) {
        Optional<InvoiceEntity> entity = repository.findById(identifier);
        return entity.map(mapper::toDTO);
    }

    @Override
    public InvoiceDTO save(InvoiceDTO invoice) {
        InvoiceEntity entity = mapper.toEntity(invoice);
        InvoiceEntity persistedEntity = repository.save(entity);
        return mapper.toDTO(persistedEntity);
    }

    @Override
    public boolean delete(Long identifier) {
        repository.deleteById(identifier);
        return true;
    }

    @Override
    public void expireOldInvoices(int timeInMinutes) {
        repository.expireOldInvoices(timeInMinutes);
    }

    @Override
    public void cancelPendingInvoicesByOrder(Long orderId) {
        repository.cancelPendingInvoicesByOrder(orderId);
    }

    @Override
    public List<InvoiceDTO> findByOrderId(Long orderId) {
        List<InvoiceEntity> invoiceEntities = repository.findByOrderId(orderId);
        return Optional.ofNullable(invoiceEntities).orElse(new ArrayList<>()).stream().map(
            mapper::toDTO).toList();
    }

}
