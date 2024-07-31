package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.ports.repository.CategoryRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.CategoryAggregate;
import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.domain.model.category.CategoryValidator;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.CategoryServicePort;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.MenuProductServicePort;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServicePortImpl implements CategoryServicePort {

  private final MenuProductServicePort menuProductService;
  private final CategoryRepositoryPort repository;
  private final CategoryValidator validator;

  @Autowired
  public CategoryServicePortImpl(MenuProductServicePort menuProductServicePort,
      CategoryRepositoryPort repository) {
    this.menuProductService = menuProductServicePort;
    this.repository = repository;
    this.validator = new CategoryValidator();
  }

  @Override
  public List<Category> getAll() {
    return repository.getAll();
  }

  @Override
  public Category getById(Long id) {
    Optional<Category> persistedCategory = repository.findById(id);
    return persistedCategory.orElseThrow(() -> new NotFoundException(
        String.format("Não foi encontrado nenhuma categoria com o id %d", id)));
  }

  @Override
  public Category getByName(String name) {
    Optional<Category> persistedCategory = repository.findByName(name);
    return persistedCategory.orElseThrow(() -> new NotFoundException(
        String.format("Não foi encontrado nenhuma categoria com o nome %s", name)));
  }

  @Override
  public void create(Category category) {
    List<MenuProduct> menuProducts = category.getProducts().stream()
        .map(mp -> menuProductService.getById(mp.getId())).toList();
    category.setProducts(menuProducts);
    CategoryAggregate categoryAggregate = new CategoryAggregate(category, validator);
    categoryAggregate.create();
    repository.save(category);
  }

  @Override
  public void update(Long id, Category category) {
    Category current = this.getById(id);
    List<MenuProduct> menuProducts = category.getProducts().stream()
        .map(mp -> menuProductService.getById(mp.getId())).toList();
    category.setProducts(menuProducts);
    CategoryAggregate categoryAggregate = new CategoryAggregate(category, validator);
    categoryAggregate.update(current);
    repository.update(category);
  }

  @Override
  public void remove(Long id) {
    Category root = this.getById(id);
    CategoryAggregate categoryAggregate = new CategoryAggregate(root);
    categoryAggregate.remove();
    repository.delete(id);
  }

}
