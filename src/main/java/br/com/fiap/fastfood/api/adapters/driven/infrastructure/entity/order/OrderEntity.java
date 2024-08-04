package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CollaboratorEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CustomerEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.OrderProductEntity;
import br.com.fiap.fastfood.api.core.domain.model.order.OrderStateEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private OrderStateEnum state;

  @Column(name = "price")
  private BigDecimal price;

  @OneToMany(mappedBy = "order")
  private List<OrderProductEntity> products;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @ManyToOne
  private CollaboratorEntity collaborator;

  @ManyToOne
  private CustomerEntity customer;

  @OneToMany(mappedBy = "order")
  private List<InvoiceEntity> orderInvoices;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

}
