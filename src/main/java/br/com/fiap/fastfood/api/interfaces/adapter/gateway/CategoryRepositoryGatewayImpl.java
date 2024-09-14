package br.com.fiap.fastfood.api.interfaces.adapter.gateway;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.category.CategoryEntity;
import br.com.fiap.fastfood.api.interfaces.adapter.mapper.CategoryMapperInfra;
import br.com.fiap.fastfood.api.infrastructure.adapter.category.CategoryRepository;
import br.com.fiap.fastfood.api.application.dto.category.CategoryDTO;
import br.com.fiap.fastfood.api.application.usecase.repository.ICategoryRepositoryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryRepositoryGatewayImpl implements ICategoryRepositoryGateway {

  private final CategoryRepository repository;
  private final CategoryMapperInfra mapper;

  @Autowired
  public CategoryRepositoryGatewayImpl(CategoryRepository repository, CategoryMapperInfra mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Optional<CategoryDTO> findById(Long identifier) {
    Optional<CategoryEntity> categoryEntityOpt = repository.findById(identifier);
    return categoryEntityOpt.map(mapper::toDTO);
  }

  @Override
  public Optional<CategoryDTO> findByName(String name) {
    Optional<CategoryEntity> categoryEntityOpt = repository.findByName(name);
    return categoryEntityOpt.map(mapper::toDTO);
  }

  @Override
  public CategoryDTO save(CategoryDTO data) {
    CategoryEntity entity = mapper.toEntity(data);
    CategoryEntity persistedCategory = repository.save(entity);
    return mapper.toDTO(persistedCategory);
  }

  @Override
  public void update(CategoryDTO category) {
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
  public List<CategoryDTO> getAll() {
    List<CategoryEntity> entityCategories = repository.findAll();
    return entityCategories.stream().map(mapper::toDTO).toList();
  }
}
