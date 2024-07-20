package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order.OrderEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductEntity extends ProductEntity {

  @ManyToOne
  private MenuProductEntity menuProduct;

  @ManyToOne
  private CampaignProductEntity campaignProduct;

  @ManyToOne
  private OrderEntity order;

}
