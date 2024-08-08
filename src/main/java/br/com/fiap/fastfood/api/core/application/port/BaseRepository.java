package br.com.fiap.fastfood.api.core.application.port;

import java.util.Optional;

public interface BaseRepository <T, K> {

  Optional<T> findById(K identifier);
  T save(T data);
  boolean delete(K identifier);

}
