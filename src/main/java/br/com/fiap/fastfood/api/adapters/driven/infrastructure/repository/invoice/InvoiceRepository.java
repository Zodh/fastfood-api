package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.invoice;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    @Query(value = "UPDATE invoice SET invoice.state = 'EXPIRED' WHERE status = 'PENDING' AND created_at > NOW() - INTERVAL :'5' MINUTE", nativeQuery = true)
    void expireOldInvoices();
}
