package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person;

import br.com.fiap.fastfood.api.core.domain.model.person.CollaboratorRoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "collaborator")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CollaboratorEntity extends PersonEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private CollaboratorRoleEnum role;

}
