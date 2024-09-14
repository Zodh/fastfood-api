package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.PersonEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "customer")
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerEntity extends PersonEntity {

}
