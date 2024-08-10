package br.com.fiap.fastfood.api.core.application.port.inbound.service;

import br.com.fiap.fastfood.api.core.application.dto.collaborator.CollaboratorDTO;
import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.OrderProductDTO;

public interface OrderServicePort {

  OrderDTO create(CustomerDTO customerDTO, CollaboratorDTO collaboratorDTO);
  OrderDTO includeOrderProduct(Long orderId, OrderProductDTO orderProduct);
  OrderDTO getById(Long orderId);
  OrderDTO removeOrderProduct(Long orderId, Long orderProductId);
  void cancel(Long orderId);
  OrderDTO confirm(Long orderId);

  // TODO: When security layer comes, this method should receive also customer that started preparation. Criado em: 07/08/2024 ás 18:39:08.
  void turnReadyToPrepare(Long orderId);
  void prepare(Long orderId);
  void turnReadyToPick(Long orderId);
  void finish(Long orderId);

}
