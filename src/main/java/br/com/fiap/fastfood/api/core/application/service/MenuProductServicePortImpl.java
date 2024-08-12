package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.mapper.MenuProductMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.MenuProductMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.port.repository.MenuProductRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.MenuProductAggregate;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProductValidator;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.MenuProductServicePort;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MenuProductServicePortImpl implements MenuProductServicePort {

  private final MenuProductRepositoryPort repository;
  private final MenuProductValidator validator;
  private final MenuProductMapperApp menuProductMapperApp;

  public MenuProductServicePortImpl(
      MenuProductRepositoryPort repository
  ) {
    this.repository = repository;
    this.validator = new MenuProductValidator();
    this.menuProductMapperApp = new MenuProductMapperAppImpl();
  }

  @Override
  public List<MenuProductDTO> getAll() {
    return repository.getAll();
  }

  @Override
  public MenuProductDTO getById(Long id) {
    Optional<MenuProductDTO> persistedProduct = repository.findById(id);
    return persistedProduct.orElseThrow(() -> new NotFoundException(
        String.format("Não foi encontrado nenhum produto com o id %d", id)));
  }

  @Override
  public void register(MenuProductDTO dto) {
    fetchIngredients(dto);

    MenuProduct menuProduct = menuProductMapperApp.toDomain(dto);
    MenuProductAggregate menuProductAggregate = new MenuProductAggregate(menuProduct, validator);
    menuProductAggregate.create();

    MenuProductDTO validMenuProduct = menuProductMapperApp.toMenuProductDTO(menuProduct);
    repository.save(validMenuProduct);
  }

  @Override
  public void remove(Long id) {
    MenuProductDTO target = this.getById(id);
    List<Long> productsThatUseTarget = repository.fetchProductsRelatedToProduct(target.getId());
    boolean removed = repository.delete(target.getId());

    // After remove target product, we need to check if other product used it as optional or ingredient.
    if (removed) {
      List<MenuProductDTO> allProductsThatUsedRemovedProduct = repository.findAllById(
          productsThatUseTarget);

      List<MenuProduct> products = allProductsThatUsedRemovedProduct.stream().map(mpDTO -> {
        MenuProduct mp = menuProductMapperApp.toDomain(mpDTO);
        mp.setIngredients(mp.getIngredients());
        return mp;
      }).collect(Collectors.toList());

      List<MenuProductDTO> productDTOS = menuProductMapperApp.toMenuProductDTO(products);
      productDTOS.forEach(repository::update);
    }
  }

  @Override
  public void update(Long id, MenuProductDTO dto) {
    MenuProductDTO currentDTO = this.getById(id);
    MenuProduct currentValue = menuProductMapperApp.toDomain(currentDTO);

    fetchIngredients(dto);
    MenuProduct updateValue = menuProductMapperApp.toDomain(dto);

    MenuProductAggregate menuProductAggregate = new MenuProductAggregate(updateValue, validator);
    menuProductAggregate.update(currentValue);

    MenuProductDTO validProductToUpdate = menuProductMapperApp.toMenuProductDTO(updateValue);
    repository.update(validProductToUpdate);
  }

  @Override
  public List<MenuProductDTO> findAllById(List<Long> ids) {
    return repository.findAllById(ids);
  }

  private void fetchIngredients(MenuProductDTO menuProduct) {
    if (Objects.nonNull(menuProduct) && Objects.nonNull(menuProduct.getIngredients()) && !menuProduct.getIngredients().isEmpty()) {
      List<MenuProductDTO> ingredients = menuProduct.getIngredients().stream()
          .map(i -> this.getById(i.getId())).toList();
      menuProduct.setIngredients(ingredients);
    }
  }

}