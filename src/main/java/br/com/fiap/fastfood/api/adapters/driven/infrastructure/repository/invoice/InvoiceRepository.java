package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.invoice;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE invoice SET current_state = 'EXPIRED' WHERE current_state = 'PENDING' AND created_at < (NOW() - ((INTERVAL '1' MINUTE) * :timeInMinutes))", nativeQuery = true)
    void expireOldInvoices(@Param("timeInMinutes") int timeInMinutes);

    @Modifying
    @Transactional
    @Query(value = "UPDATE invoice SET current_state = 'CANCELLED' WHERE order_id = :id "
        + "AND current_state = 'PENDING'", nativeQuery = true)
    void cancelPendingInvoicesByOrder(@Param("id") Long id);

    List<InvoiceEntity> findByOrderId(Long orderId);

}
