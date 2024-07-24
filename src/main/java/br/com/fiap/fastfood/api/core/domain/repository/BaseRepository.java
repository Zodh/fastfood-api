package br.com.fiap.fastfood.api.core.domain.repository;

public interface BaseRepository <T, K> {

  T findById(K identifier);
  T save(T data);

}
