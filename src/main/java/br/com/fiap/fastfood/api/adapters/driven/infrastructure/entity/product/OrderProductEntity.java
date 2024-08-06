package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order.OrderEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderProductEntity extends ProductEntity {

  @ManyToOne
  @NotNull
  private MenuProductEntity menuProduct;

  @ManyToOne
  private OrderEntity order;

  @ManyToMany
  @JoinTable(
      name = "order_product_optional",
      joinColumns = @JoinColumn(name = "order_product_id"),
      inverseJoinColumns = @JoinColumn(name = "order_optional_id")
  )
  private List<OrderProductEntity> optionals;

  @ManyToMany
  @JoinTable(
      name = "order_product_ingredient",
      joinColumns = @JoinColumn(name = "order_product_id"),
      inverseJoinColumns = @JoinColumn(name = "order_ingredient_id")
  )
  private List<OrderProductEntity> ingredients;

  @Column(name = "should_remove")
  private boolean shouldRemove;

}
