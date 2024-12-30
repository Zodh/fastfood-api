package br.com.fiap.fastfood.api.application.dto.followup;


import br.com.fiap.fastfood.api.application.dto.order.OrderDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class FollowUpDTO {

  private Long id;
  private int showOrder;
  private OrderDTO order;
  private FollowUpStateEnum state;

}
