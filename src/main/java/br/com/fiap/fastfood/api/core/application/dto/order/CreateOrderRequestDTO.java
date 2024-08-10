package br.com.fiap.fastfood.api.adapters.driven.infrastructure.dto.order;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.dto.collaborator.CollaboratorDTO;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.dto.customer.CustomerDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class CreateOrderRequestDTO {

  private CustomerDTO customer;
  private CollaboratorDTO collaborator;

}
