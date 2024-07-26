package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.MenuProductEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.MenuProductMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.product.MenuProductRepository;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.MenuProductRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MenuProductRepositoryAdapterImpl implements MenuProductRepositoryPort {

  private final MenuProductRepository repository;
  private final MenuProductMapper mapper;

  @Autowired
  public MenuProductRepositoryAdapterImpl(MenuProductRepository repository,
                                          MenuProductMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Optional<MenuProduct> findById(Long identifier) {
    return Optional.empty();
  }

  @Override
  public MenuProduct save(MenuProduct data) {
    return null;
  }

  @Override
  public void delete(Long identifier) {

  }

  @Override
  public List<MenuProduct> getAll() {
    List<MenuProductEntity> entityProducts = repository.findAll();
    return entityProducts.stream().map(mapper::toDomain).toList();
  }
}