package br.com.fiap.fastfood.api.application.gateway.repository;

import java.util.Optional;

public interface BaseRepositoryGateway<T, K> {

  Optional<T> findById(K identifier);
  T save(T data);
  boolean delete(K identifier);

}
