package br.com.fiap.fastfood.api.core.domain.repository;

import br.com.fiap.fastfood.api.core.domain.entity.Customer;

public interface CustomerRepositoryOutboundPort {

  void register(Customer customer);

}
