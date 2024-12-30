package br.com.fiap.fastfood.api.application.dto.order;

import br.com.fiap.fastfood.api.application.dto.collaborator.CollaboratorDTO;
import br.com.fiap.fastfood.api.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.application.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.entities.order.OrderStateEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class OrderDTO {

  private Long id;
  private OrderStateEnum state;
  private List<OrderProductDTO> products;
  private CollaboratorDTO collaborator;
  private CustomerDTO customer;
  private BigDecimal price;
  private LocalDateTime createdAt;
  @JsonIgnore
  private List<InvoiceDTO> invoices;
  private InvoiceDTO invoice;

}
