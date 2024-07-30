package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.domain.aggregate.CategoryAggregate;
import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.CategoryServicePort;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.CategoryRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.MenuProductRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.service.CategoryService;
import br.com.fiap.fastfood.api.core.domain.service.MenuProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServicePortImpl implements CategoryServicePort {

  private final CategoryService categoryService;
  private final MenuProductService menuProductService;
  private final CategoryRepositoryPort categoryRepositoryPort;

  @Autowired
  public CategoryServicePortImpl(MenuProductRepositoryPort menuProductRepositoryPort, CategoryRepositoryPort categoryRepositoryPort) {
      this.categoryService = new CategoryService(categoryRepositoryPort);
      this.menuProductService = new MenuProductService(menuProductRepositoryPort);
      this.categoryRepositoryPort = categoryRepositoryPort;
  }

  @Override
  public List<Category> getAll() {
    return categoryService.getAll();
  }

  @Override
  public Category getById(Long id) {
    return categoryService.getById(id);
  }

  @Override
  public Category getByName(String name) {
    return categoryService.getByName(name);
  }

  @Override
  public void create(Category category) {
    CategoryAggregate categoryAggregate = new CategoryAggregate(category, categoryRepositoryPort, categoryService, menuProductService);
    categoryAggregate.create();
  }

  @Override
  public void update(Long id, Category category) {
    CategoryAggregate categoryAggregate = new CategoryAggregate(category, categoryRepositoryPort, categoryService, menuProductService);
    categoryAggregate.update(id);
  }

  @Override
  public void remove(Long id) {
    CategoryAggregate categoryAggregate = new CategoryAggregate(categoryRepositoryPort, categoryService);
    categoryAggregate.remove(id);
  }
}
