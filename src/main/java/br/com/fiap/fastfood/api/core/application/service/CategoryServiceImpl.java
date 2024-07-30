package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.domain.aggregate.CategoryAggregate;
import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.CategoryServicePort;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.CategoryRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryServicePort {

  private final CategoryService categoryService;
  private final CategoryRepositoryPort categoryRepositoryPort;

  @Autowired
  public CategoryServiceImpl(CategoryService categoryService, CategoryRepositoryPort categoryRepositoryPort) {
    this.categoryService = categoryService;
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
  public void create(Category category) {
    CategoryAggregate categoryAggregate = new CategoryAggregate(category, categoryRepositoryPort, categoryService);
    categoryAggregate.create();
  }

  @Override
  public void update(Long id, Category category) {
    CategoryAggregate categoryAggregate = new CategoryAggregate(category, categoryRepositoryPort, categoryService);
    categoryAggregate.update(id);
  }

  @Override
  public void remove(Long id) {
    // TODO
  }
}
