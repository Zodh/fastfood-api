package br.com.fiap.fastfood.api.adapters.driver.dto.invoice;

import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceStateEnum;
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

  private InvoiceStateEnum state;
  private BigDecimal price;
  private LocalDateTime createdAt;

}
