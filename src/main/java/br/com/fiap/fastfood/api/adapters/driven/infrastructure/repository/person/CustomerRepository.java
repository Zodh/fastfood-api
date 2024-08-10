package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.person;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CustomerEntity;
import br.com.fiap.fastfood.api.core.application.dto.customer.DocumentTypeEnum;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

  @Query(value = "SELECT ce FROM CustomerEntity ce WHERE ce.email = :email ")
  Optional<CustomerEntity> findByEmail(@Param(value = "email") String email);

  @Query(value = "SELECT ce "
      + "FROM CustomerEntity ce "
      + "WHERE ce.documentNumber = :documentNumber "
      + "AND ce.active = TRUE "
      + "AND ce.documentType = :documentType ")
  Optional<CustomerEntity> findByDocument(
      @Param(value = "documentNumber") String documentNumber,
      @Param("documentType") DocumentTypeEnum documentType);

  @Modifying
  @Query("UPDATE CustomerEntity ce "
      + "SET ce.active = TRUE "
      + "WHERE ce.id = :id ")
  @Transactional
  void activate(@Param("id") Long id);

}
