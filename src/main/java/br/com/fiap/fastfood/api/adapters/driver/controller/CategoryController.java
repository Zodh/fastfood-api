package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.CategoryRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.MenuProductRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.core.application.dto.category.CategoryDTO;
import br.com.fiap.fastfood.api.core.application.dto.category.CategoryResponseDTO;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.CategoryServicePort;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.MenuProductServicePort;
import br.com.fiap.fastfood.api.core.application.service.CategoryServicePortImpl;
import br.com.fiap.fastfood.api.core.application.service.MenuProductServicePortImpl;
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

  private final CategoryServicePort categoryServicePort;

  @Autowired
  public CategoryController(
      MenuProductRepositoryAdapterImpl menuProductRepositoryAdapter,
      CategoryRepositoryAdapterImpl categoryRepositoryAdapter) {
    MenuProductServicePort menuProductServicePort = new MenuProductServicePortImpl(
        menuProductRepositoryAdapter);
    this.categoryServicePort = new CategoryServicePortImpl(menuProductServicePort,
        categoryRepositoryAdapter);
  }

  @GetMapping
  public ResponseEntity<CategoryResponseDTO> getAll() {
    List<CategoryDTO> allCategories = categoryServicePort.getAll();
    CategoryResponseDTO response = new CategoryResponseDTO(allCategories);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{identifier}")
  public ResponseEntity<CategoryDTO> getByIdOrName(@PathVariable String identifier) {
    CategoryDTO categoryDTO;
    try {
      Long id = Long.parseLong(identifier);
      categoryDTO = categoryServicePort.getById(id);
    } catch (NumberFormatException e) {
      categoryDTO = categoryServicePort.getByName(identifier);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
  }

  @PostMapping
  public ResponseEntity<Void> create(@Valid @RequestBody CategoryDTO categoryDTO) {
    categoryServicePort.create(categoryDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
    categoryServicePort.update(id, categoryDTO);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    categoryServicePort.remove(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
