package br.com.fiap.fastfood.api.adapters.gateway.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest {

  private Long externalPaymentId;
  private Long externalProductId;
  private BigDecimal amount;
  private String currency;
  private String description;
  private String callbackUrl;
  private LocalDateTime dueAt;

}
