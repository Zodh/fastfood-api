package br.com.fiap.fastfood.api.core.application.aggregate.service;

import br.com.fiap.fastfood.api.core.domain.entity.Customer;

public interface ServiceAggregateInboundPort {

  void register(Customer customer);
  void activate(Long customerId);

}
