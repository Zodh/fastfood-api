package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.invoice;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    @Query(value = "SELECT * FROM invoice WHERE status = :status", nativeQuery = true)
    List<InvoiceEntity> fetchInvoicesByStatus(@Param("status") String status);
}
