package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.dto.category.CategoryDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.mapper.CategoryMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.CategoryMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.mapper.MenuProductMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.MenuProductMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.port.repository.CategoryRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.CategoryAggregate;
import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.domain.model.category.CategoryValidator;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.CategoryServicePort;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.MenuProductServicePort;
import java.util.List;
import java.util.Optional;

public class CategoryServicePortImpl implements CategoryServicePort {

  private final MenuProductServicePort menuProductService;
  private final CategoryRepositoryPort repository;
  private final CategoryValidator validator;
  private final CategoryMapperApp categoryMapperApp;
  private final MenuProductMapperApp menuProductMapperApp;

  public CategoryServicePortImpl(MenuProductServicePort menuProductServicePort,
      CategoryRepositoryPort repository) {
    this.menuProductService = menuProductServicePort;
    this.repository = repository;
    this.validator = new CategoryValidator();
    this.categoryMapperApp = new CategoryMapperAppImpl();
    this.menuProductMapperApp = new MenuProductMapperAppImpl();
  }

  @Override
  public List<CategoryDTO> getAll() {
    return repository.getAll();
  }

  @Override
  public CategoryDTO getById(Long id) {
    Optional<CategoryDTO> persistedCategory = repository.findById(id);
    return persistedCategory.orElseThrow(() -> new NotFoundException(
        String.format("Não foi encontrado nenhuma categoria com o id %d", id)));
  }

  @Override
  public CategoryDTO getByName(String name) {
    Optional<CategoryDTO> persistedCategory = repository.findByName(name);
    return persistedCategory.orElseThrow(() -> new NotFoundException(
        String.format("Não foi encontrado nenhuma categoria com o nome %s", name)));
  }

  @Override
  public void create(CategoryDTO dto) {
    List<MenuProduct> menuProducts = getMenuProducts(dto);

    Category category = categoryMapperApp.toDomain(dto);
    category.setProducts(menuProducts);
    CategoryAggregate categoryAggregate = new CategoryAggregate(category, validator);
    categoryAggregate.create();

    CategoryDTO validCategory = categoryMapperApp.toCategoryDTO(category);
    repository.save(validCategory);
  }

  @Override
  public void update(Long id, CategoryDTO dto) {
    CategoryDTO currentDTO = this.getById(id);
    Category current = categoryMapperApp.toDomain(currentDTO);

    List<MenuProduct> menuProducts = getMenuProducts(dto);

    Category updatedCategory = categoryMapperApp.toDomain(dto);
    updatedCategory.setProducts(menuProducts);
    CategoryAggregate categoryAggregate = new CategoryAggregate(updatedCategory, validator);
    categoryAggregate.update(current);

    CategoryDTO updatedCategoryDTO = categoryMapperApp.toCategoryDTO(updatedCategory);
    repository.update(updatedCategoryDTO);
  }

  private List<MenuProduct> getMenuProducts(CategoryDTO dto) {
    List<MenuProductDTO> menuProductDTOList = dto.getProducts().stream()
        .map(mp -> menuProductService.getById(mp.getId())).toList();
    return menuProductDTOList.stream().map(menuProductMapperApp::toDomain).toList();
  }

  @Override
  public void remove(Long id) {
    CategoryDTO categoryDTO = this.getById(id);
    Category category = categoryMapperApp.toDomain(categoryDTO);

    CategoryAggregate categoryAggregate = new CategoryAggregate(category);
    categoryAggregate.remove();
    repository.delete(id);
  }

}
