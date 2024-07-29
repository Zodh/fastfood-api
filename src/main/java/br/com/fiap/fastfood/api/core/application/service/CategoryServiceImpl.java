package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.domain.aggregate.CategoryAggregate;
import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.CategoryServicePort;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.CategoryRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.service.CategoryService;
import java.util.List;

public class CategoryServiceImpl implements CategoryServicePort {

  private final CategoryService categoryService;
  private final CategoryAggregate categoryAggregate;
  private final CategoryRepositoryPort categoryRepositoryPort;

  public CategoryServiceImpl(CategoryService categoryService, CategoryAggregate categoryAggregate, CategoryRepositoryPort categoryRepositoryPort) {
    this.categoryService = categoryService;
    this.categoryAggregate = categoryAggregate;
    this.categoryRepositoryPort = categoryRepositoryPort;
  }


  @Override
  public List<Category> getAll() {
    return List.of();
  }

  @Override
  public Category getById(Long id) {
    return null;
  }

  @Override
  public void create(Category category) {

  }

  @Override
  public void update(Long id, Category category) {

  }

  @Override
  public void remove(Long id) {

  }
}
