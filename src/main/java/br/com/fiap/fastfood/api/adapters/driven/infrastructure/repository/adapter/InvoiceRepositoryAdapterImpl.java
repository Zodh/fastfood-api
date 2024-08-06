package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.InvoiceMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.invoice.InvoiceRepository;
import br.com.fiap.fastfood.api.core.application.ports.repository.InvoiceRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
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
        domain.ifPresent(val ->
                val.setState(mapper.mapStateImpl(entity.get().getState(), domain.get()))
        );

        return domain;
    }

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceEntity entity = mapper.toEntity(invoice);
        InvoiceEntity persistedEntity = repository.save(entity);
        Invoice toDomain = mapper.toDomain(persistedEntity);
        toDomain.setState(mapper.mapStateImpl(persistedEntity.getState(), toDomain));
        return toDomain;
    }

    @Override
    public boolean delete(Long identifier) {
        repository.deleteById(identifier);
        return true;
    }

    @Override
    public void expireOldInvoices() {
        repository.expireOldInvoices();
    }
}
