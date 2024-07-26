package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.person.activation;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.activation.ActivationCodeEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ActivationCodeRepository extends JpaRepository<ActivationCodeEntity, UUID> {

  @Transactional
  @Modifying
  @Query("DELETE ActivationCodeEntity ace WHERE ace.customer.id = :customerId")
  void deleteAllByCustomerId(@Param("customerId") Long customerId);

}
