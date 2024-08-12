package br.com.fiap.fastfood.api.core.application.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaidOrderResponseDTO {

  @Builder.Default
  private String message = "Seu pedido foi pago e será preparado em breve!";
  private Long orderNumber;

}
