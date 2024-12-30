package br.com.fiap.fastfood.api.adapters.controller;

import br.com.fiap.fastfood.api.adapters.gateway.CategoryRepositoryGatewayImpl;
import br.com.fiap.fastfood.api.adapters.gateway.MenuProductRepositoryGatewayImpl;
import br.com.fiap.fastfood.api.application.dto.category.CategoryDTO;
import br.com.fiap.fastfood.api.application.dto.category.CategoryResponseDTO;
import br.com.fiap.fastfood.api.application.gateway.mapper.CategoryMapperAppImpl;
import br.com.fiap.fastfood.api.application.gateway.mapper.MenuProductMapperApp;
import br.com.fiap.fastfood.api.application.gateway.mapper.MenuProductMapperAppImpl;
import br.com.fiap.fastfood.api.application.service.CategoryService;
import br.com.fiap.fastfood.api.application.service.MenuProductService;
import br.com.fiap.fastfood.api.application.service.impl.CategoryServiceImpl;
import br.com.fiap.fastfood.api.application.service.impl.MenuProductServiceImpl;
import java.util.List;

public class CategoryController {

  private final CategoryService categoryService;


  public CategoryController(
      MenuProductRepositoryGatewayImpl menuProductRepositoryAdapter,
      CategoryRepositoryGatewayImpl categoryRepositoryAdapter) {
    MenuProductMapperApp menuProductMapperApp = new MenuProductMapperAppImpl();
    MenuProductService MenuProductService = new MenuProductServiceImpl(
        menuProductRepositoryAdapter, menuProductMapperApp);
    this.categoryService = new CategoryServiceImpl(MenuProductService,
        categoryRepositoryAdapter, new CategoryMapperAppImpl(), menuProductMapperApp);
  }


  public CategoryResponseDTO getAll() {
    List<CategoryDTO> allCategories = categoryService.getAll();
    CategoryResponseDTO response = new CategoryResponseDTO(allCategories);
    return response;
  }


  public CategoryDTO getByIdOrName(String identifier) {
    CategoryDTO categoryDTO;
    try {
      Long id = Long.parseLong(identifier);
      categoryDTO = categoryService.getById(id);
    } catch (NumberFormatException e) {
      categoryDTO = categoryService.getByName(identifier);
    }
    return categoryDTO;
  }


  public void create(CategoryDTO categoryDTO) {
    categoryService.create(categoryDTO);
  }


  public void update(Long id, CategoryDTO categoryDTO) {
    categoryService.update(id, categoryDTO);
  }

  public void delete(Long id) {
    categoryService.remove(id);
  }

}
