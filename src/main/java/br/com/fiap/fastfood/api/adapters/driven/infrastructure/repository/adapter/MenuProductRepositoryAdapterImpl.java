package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.MenuProductEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.MenuProductMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.product.MenuProductRepository;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.MenuProductRepositoryPort;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
    Optional<MenuProductEntity> menuProductEntityOpt = repository.findById(identifier);
    return menuProductEntityOpt.map(mapper::toDomain);
  }

  @Override
  public MenuProduct save(MenuProduct data) {
    MenuProductEntity entity = mapper.toEntity(data);
    MenuProductEntity persistedMenuProduct = repository.save(entity);
    return mapper.toDomain(persistedMenuProduct);
  }

  @Override
  public boolean delete(Long identifier) {
    Optional<MenuProductEntity> menuProductEntityOpt = repository.findById(identifier);
    if (menuProductEntityOpt.isEmpty()) {
      return false;
    }
    repository.deleteById(identifier);
    return true;
  }

  @Override
  public List<MenuProduct> getAll() {
    List<MenuProductEntity> entityProducts = repository.findAll();
    return entityProducts.stream().map(mapper::toDomain).toList();
  }

  @Override
  public void update(MenuProduct menuProduct) {
    MenuProductEntity entity = mapper.toEntity(menuProduct);
    repository.save(entity);
  }

  @Override
  public List<Long> fetchProductsRelatedToProduct(Long productId) {
    return Stream.concat(
        repository.fetchProductsByIngredient(productId).stream(),
        repository.fetchProductsByOptional(productId
        ).stream()).distinct().collect(Collectors.toList());
  }

  @Override
  public List<MenuProduct> findAllById(List<Long> ids) {
    List<MenuProductEntity> products = repository.findAllById(ids);
    return products.stream().map(mapper::toDomain).collect(Collectors.toList());
  }

}
