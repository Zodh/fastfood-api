package br.com.fiap.fastfood.api.core.application.ports.repository;

import br.com.fiap.fastfood.api.core.application.ports.BaseRepository;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.Document;
import java.util.Optional;

public interface CustomerRepositoryPort extends BaseRepository<Customer, Long> {

  Optional<Customer> findByEmail(String email);

  Optional<Customer> findByDocument(Document document);

  void activate(Long identifier);

}
