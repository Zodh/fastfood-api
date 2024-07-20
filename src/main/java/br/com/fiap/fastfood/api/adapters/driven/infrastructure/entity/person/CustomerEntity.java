package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "customer")
@Data
public class CustomerEntity extends PersonEntity {

}
