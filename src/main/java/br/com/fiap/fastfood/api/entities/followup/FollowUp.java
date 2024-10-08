package br.com.fiap.fastfood.api.entities.followup;

import br.com.fiap.fastfood.api.entities.followup.state.FollowUpState;
import br.com.fiap.fastfood.api.entities.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowUp {

  private Long id;
  private int showOrder;
  private Order order;
  private FollowUpState state;

}
