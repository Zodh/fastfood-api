package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.ports.repository.MenuProductRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.MenuProductAggregate;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProductValidator;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.MenuProductServicePort;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class MenuProductServicePortImpl implements MenuProductServicePort {

  private final MenuProductRepositoryPort repository;
  private final MenuProductValidator validator;

  @Autowired
  public MenuProductServicePortImpl(
      MenuProductRepositoryPort repository
  ) {
    this.repository = repository;
    this.validator = new MenuProductValidator();
  }

  @Override
  public Page<MenuProduct> getAll(Pageable pageable) {
    return repository.getAll(pageable);
  }

  @Override
  public MenuProduct getById(Long id) {
    Optional<MenuProduct> persistedProduct = repository.findById(id);
    return persistedProduct.orElseThrow(() -> new NotFoundException(
        String.format("Não foi encontrado nenhum produto com o id %d", id)));
  }

  @Override
  public void register(MenuProduct menuProduct) {
    fetchIngredients(menuProduct);
    MenuProductAggregate menuProductAggregate = new MenuProductAggregate(menuProduct, validator);
    menuProductAggregate.create();
    repository.save(menuProduct);
  }

  @Override
  public void remove(Long id) {
    MenuProduct target = this.getById(id);
    List<Long> productsThatUseTarget = repository.fetchProductsRelatedToProduct(target.getId());
    boolean removed = repository.delete(target.getId());

    // After remove target product, we need to check if other product used it as optional or ingredient.
    if (removed) {
      List<MenuProduct> allProductsThatUsedRemovedProduct = repository.findAllById(
          productsThatUseTarget);
      allProductsThatUsedRemovedProduct.forEach(p -> {
        p.setIngredients(p.getIngredients());
      });
      allProductsThatUsedRemovedProduct.forEach(repository::update);
    }
  }

  @Override
  public void update(Long id, MenuProduct menuProduct) {
    MenuProduct current = this.getById(id);
    fetchIngredients(menuProduct);
    MenuProductAggregate menuProductAggregate = new MenuProductAggregate(menuProduct, validator);
    menuProductAggregate.update(current);
    repository.update(menuProduct);
  }

  @Override
  public List<MenuProduct> findAllById(List<Long> ids) {
    return repository.findAllById(ids);
  }

  private void fetchIngredients(MenuProduct menuProduct) {
    if (Objects.nonNull(menuProduct) && !CollectionUtils.isEmpty(menuProduct.getIngredients())) {
      List<MenuProduct> ingredients = menuProduct.getIngredients().stream()
          .map(i -> this.getById(i.getId())).toList();
      menuProduct.setIngredients(ingredients);
    }
  }

}