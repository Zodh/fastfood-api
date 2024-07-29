package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order.OrderEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
  private CampaignProductEntity campaignProduct;

  @ManyToOne
  private OrderEntity order;

}
