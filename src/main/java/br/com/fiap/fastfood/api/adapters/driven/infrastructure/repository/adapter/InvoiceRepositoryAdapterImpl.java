package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.InvoiceMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.invoice.InvoiceRepository;
import br.com.fiap.fastfood.api.core.application.port.repository.InvoiceRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
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
    public Optional<Invoice> findById(Long identifier) {
        Optional<InvoiceEntity> entity = repository.findById(identifier);
        Optional<Invoice> domain = entity.map(mapper::toDomain);
        domain.ifPresent(invoice ->
                mapper.mapStateImpl(entity.get().getState(), domain.get())
        );
        return domain;
    }

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceEntity entity = mapper.toEntity(invoice);
        InvoiceEntity persistedEntity = repository.save(entity);
        Invoice toDomain = mapper.toDomain(persistedEntity);
        mapper.mapStateImpl(persistedEntity.getState(), toDomain);
        return toDomain;
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
    public List<Invoice> findByOrderId(Long orderId) {
        List<InvoiceEntity> invoiceEntities = repository.findByOrderId(orderId);
        return Optional.ofNullable(invoiceEntities).orElse(Collections.emptyList()).stream().map(invoiceEntity -> {
            Invoice invoice = mapper.toDomain(invoiceEntity);
            mapper.mapStateImpl(invoiceEntity.getState(), invoice);
            return invoice;
        }).toList();
    }

}
