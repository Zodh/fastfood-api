package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.user.UserEntity;
import br.com.fiap.fastfood.api.core.application.dto.customer.DocumentTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class PersonEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @Column(name = "name")
  protected String name;

  @Email
  @Column(name = "email")
  protected String email;

  @Column(name = "birthdate")
  protected LocalDate birthdate;

  @Column(name = "document_number")
  protected String documentNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "document_type")
  protected DocumentTypeEnum documentType;

  @Column(name = "phone_number")
  protected String phoneNumber;

  @OneToOne(mappedBy = "person")
  protected UserEntity user;

  @Column(name = "created_at")
  protected LocalDateTime created;

  @Column(name = "updated_at")
  protected LocalDateTime updated;

  @Column(name = "active")
  protected boolean active;

  @PrePersist
  protected void onCreate() {
    created = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updated = LocalDateTime.now();
  }

  public String getDocumentNumber() {
    return documentNumber;
  }

  public void setDocumentNumber(String documentNumber) {
    this.documentNumber = documentNumber;
  }
}
