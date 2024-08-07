package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.category.CategoryEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CategoryMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.category.CategoryRepository;
import br.com.fiap.fastfood.api.core.application.ports.repository.CategoryRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoryRepositoryAdapterImpl implements CategoryRepositoryPort {

  private final CategoryRepository repository;
  private final CategoryMapper mapper;

  @Autowired
  public CategoryRepositoryAdapterImpl(CategoryRepository repository,
                                       CategoryMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Optional<Category> findById(Long identifier) {
    Optional<CategoryEntity> categoryEntityOpt = repository.findById(identifier);
    return categoryEntityOpt.map(mapper::toDomain);
  }

  @Override
  public Optional<Category> findByName(String name) {
    Optional<CategoryEntity> categoryEntityOpt = repository.findByName(name);
    return categoryEntityOpt.map(mapper::toDomain);
  }

  @Override
  public Category save(Category data) {
    CategoryEntity entity = mapper.toEntity(data);
    CategoryEntity persistedCategory = repository.save(entity);
    return mapper.toDomain(persistedCategory);
  }

  @Override
  public void update(Category category) {
    CategoryEntity entity = mapper.toEntity(category);
    repository.save(entity);
  }

  @Override
  public List<Long> fetchProductsRelatedToCategory(Long categoryId) {
    return repository.fetchProductsByCategory(categoryId);
  }

  @Override
  public boolean delete(Long identifier) {
    Optional<CategoryEntity> categoryEntityOpt = repository.findById(identifier);
    if (categoryEntityOpt.isEmpty()) {
      return false;
    }
    repository.deleteById(identifier);
    return true;
  }

  @Override
  public Page<Category> getAll(Pageable pageable) {
    Page<CategoryEntity> entityCategories = repository.findAll(pageable);
    List<Category> domainCategories = entityCategories.stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    return new PageImpl<>(domainCategories, pageable, entityCategories.getTotalElements());
  }
}
