package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  protected Long preparationTimeInMillis;

  @Column(name = "quantity")
  protected int quantity;

  @Column(name = "cost")
  protected BigDecimal cost;

  @Column(name = "ingredient")
  private boolean ingredient;

  @Column(name = "optional")
  private boolean optional;

  @Column(name = "created_at", updatable = false)
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
