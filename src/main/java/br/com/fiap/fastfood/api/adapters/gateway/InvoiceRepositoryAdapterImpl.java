package br.com.fiap.fastfood.api.adapters.gateway;

import br.com.fiap.fastfood.api.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.infrastructure.dao.entity.invoice.InvoiceEntity;
import br.com.fiap.fastfood.api.adapters.mapper.InvoiceMapperInfra;
import br.com.fiap.fastfood.api.infrastructure.dao.repository.invoice.InvoiceRepository;
import br.com.fiap.fastfood.api.application.gateway.repository.InvoiceRepositoryGateway;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvoiceRepositoryAdapterImpl implements InvoiceRepositoryGateway {

  private final InvoiceRepository repository;
  private final InvoiceMapperInfra mapper;

  @Autowired
  public InvoiceRepositoryAdapterImpl(InvoiceRepository repository, InvoiceMapperInfra mapper) {
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
