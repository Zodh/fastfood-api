package br.com.fiap.fastfood.api.core.domain.ports.inbound;

import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;

public interface OrderServicePort {

  Order create(Customer customer, Collaborator collaborator);

  Order getById(Long id);
}
