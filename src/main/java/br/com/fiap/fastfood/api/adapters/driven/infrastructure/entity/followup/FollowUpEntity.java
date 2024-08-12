package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.followup;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order.OrderEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "followup")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowUpEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "show_order")
  private int showOrder;

  @ManyToOne
  private OrderEntity order;

  @Enumerated(EnumType.STRING)
  @Column(name = "state")
  private FollowUpStateEnum state;

  public FollowUpEntity(Long id, int showOrder, Long orderId, FollowUpStateEnum state) {
    OrderEntity o = new OrderEntity();
    o.setId(orderId);
    this.id = id;
    this.showOrder = showOrder;
    this.order = o;
    this.state = state;
  }
}
