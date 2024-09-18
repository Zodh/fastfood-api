package br.com.fiap.fastfood.api.adapters.controller;

import br.com.fiap.fastfood.api.adapters.gateway.CategoryRepositoryGatewayImpl;
import br.com.fiap.fastfood.api.adapters.gateway.MenuProductRepositoryGatewayImpl;
import br.com.fiap.fastfood.api.application.dto.category.CategoryDTO;
import br.com.fiap.fastfood.api.application.dto.category.CategoryResponseDTO;
import br.com.fiap.fastfood.api.application.service.ICategoryService;
import br.com.fiap.fastfood.api.application.service.IMenuProductService;
import br.com.fiap.fastfood.api.application.service.impl.CategoryServiceImpl;
import br.com.fiap.fastfood.api.application.service.impl.MenuProductServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final ICategoryService categoryService;

  @Autowired
  public CategoryController(
      MenuProductRepositoryGatewayImpl menuProductRepositoryAdapter,
      CategoryRepositoryGatewayImpl categoryRepositoryAdapter) {
    IMenuProductService IMenuProductService = new MenuProductServiceImpl(
        menuProductRepositoryAdapter);
    this.categoryService = new CategoryServiceImpl(IMenuProductService,
        categoryRepositoryAdapter);
  }

  @GetMapping
  public ResponseEntity<CategoryResponseDTO> getAll() {
    List<CategoryDTO> allCategories = categoryService.getAll();
    CategoryResponseDTO response = new CategoryResponseDTO(allCategories);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{identifier}")
  public ResponseEntity<CategoryDTO> getByIdOrName(@PathVariable String identifier) {
    CategoryDTO categoryDTO;
    try {
      Long id = Long.parseLong(identifier);
      categoryDTO = categoryService.getById(id);
    } catch (NumberFormatException e) {
      categoryDTO = categoryService.getByName(identifier);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
  }

  @PostMapping
  public ResponseEntity<Void> create(@Valid @RequestBody CategoryDTO categoryDTO) {
    categoryService.create(categoryDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
    categoryService.update(id, categoryDTO);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    categoryService.remove(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
