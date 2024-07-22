package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "collaborator")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollaboratorEntity extends PersonEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private CollaboratorRoleEnum role;

}
