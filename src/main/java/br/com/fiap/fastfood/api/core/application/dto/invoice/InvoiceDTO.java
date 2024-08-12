package br.com.fiap.fastfood.api.core.application.dto.invoice;

import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceStateEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceDTO {

  private int id;
  private InvoiceStateEnum state;
  private BigDecimal price;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  @JsonIgnore
  private OrderDTO order;

}
