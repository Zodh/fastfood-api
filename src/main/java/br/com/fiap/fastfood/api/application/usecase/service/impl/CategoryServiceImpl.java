package br.com.fiap.fastfood.api.application.usecase.service.impl;

import br.com.fiap.fastfood.api.application.dto.category.CategoryDTO;
import br.com.fiap.fastfood.api.application.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.application.usecase.mapper.CategoryMapperApp;
import br.com.fiap.fastfood.api.application.usecase.mapper.CategoryMapperAppImpl;
import br.com.fiap.fastfood.api.application.usecase.mapper.MenuProductMapperApp;
import br.com.fiap.fastfood.api.application.usecase.mapper.MenuProductMapperAppImpl;
import br.com.fiap.fastfood.api.application.usecase.repository.ICategoryRepositoryGateway;
import br.com.fiap.fastfood.api.application.usecase.service.ICategoryService;
import br.com.fiap.fastfood.api.application.usecase.service.IMenuProductService;
import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.domain.entity.category.Category;
import br.com.fiap.fastfood.api.domain.entity.category.CategoryValidator;
import br.com.fiap.fastfood.api.domain.entity.product.MenuProduct;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CategoryServiceImpl implements ICategoryService {

  private final IMenuProductService menuProductService;
  private final ICategoryRepositoryGateway repository;
  private final CategoryValidator validator;
  private final CategoryMapperApp categoryMapperApp;
  private final MenuProductMapperApp menuProductMapperApp;

  public CategoryServiceImpl(IMenuProductService IMenuProductService,
                             ICategoryRepositoryGateway repository) {
    this.menuProductService = IMenuProductService;
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

    List<ErrorDetail> errors = validator.validate(category);
    if (Objects.nonNull(errors) && !errors.isEmpty()) {
      throw new DomainException(errors);
    }

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

    updatedCategory.setId(current.getId());
    List<ErrorDetail> errors = validator.validate(updatedCategory);

    if (Objects.nonNull(errors) && !errors.isEmpty()) {
      throw new DomainException(errors);
    }

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

    if (category.isEnabled()) {
      throw new DomainException(new ErrorDetail("category", "Não é possível remover uma categoria ativa!"));
    }

    repository.delete(id);
  }

}
