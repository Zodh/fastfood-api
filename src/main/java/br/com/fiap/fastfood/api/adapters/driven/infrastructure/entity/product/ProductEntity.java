package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class ProductEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @Column(name = "name")
  protected String name;

  @Column(name = "description")
  protected String description;

  @Column(name = "price")
  protected BigDecimal price;

  @Column(name = "preparation_time")
  protected Long preparationTime;

  @Column(name = "quantity")
  protected int quantity;

  @Column(name = "cost")
  protected BigDecimal cost;

  @Column(name = "created_at")
  private LocalDateTime created;

  @Column(name = "updated_at")
  private LocalDateTime updated;

  @PrePersist
  protected void onCreate() {
    created = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updated = LocalDateTime.now();
  }

}
