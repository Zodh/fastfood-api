package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CategoryMapper;
import br.com.fiap.fastfood.api.adapters.driver.dto.category.CategoryDTO;
import br.com.fiap.fastfood.api.core.application.service.CategoryServicePortImpl;
import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.CategoryServicePort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryServicePort categoryServicePort;
  private final CategoryMapper mapper;

  @Autowired
  public CategoryController(
      CategoryServicePortImpl categoryServiceInboundPortImpl, CategoryMapper mapper) {
    this.categoryServicePort = categoryServiceInboundPortImpl;
    this.mapper = mapper;
  }

  @GetMapping
  public ResponseEntity<Page<CategoryDTO>> getAll(
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "10") int size,
          @RequestParam(value = "sort", defaultValue = "id") String sort,
          @RequestParam(value = "direction", defaultValue = "asc") String direction) {
    Sort.Direction sortDirection = Sort.Direction.fromString(direction);
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
    Page<Category> categoryPage = categoryServicePort.getAll(pageable);
    Page<CategoryDTO> categoryDTOPage = categoryPage.map(mapper::toCategoryDTO);
    return ResponseEntity.ok(categoryDTOPage);
  }

  @GetMapping("/{identifier}")
  public ResponseEntity<CategoryDTO> getByIdOrName(@PathVariable String identifier) {
    Category category;
    try {
      Long id = Long.parseLong(identifier);
      category = categoryServicePort.getById(id);
    } catch (NumberFormatException e) {
      category = categoryServicePort.getByName(identifier);
    }
    CategoryDTO categoryDTO = mapper.toCategoryDTO(category);
    return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
  }

  @PostMapping
  public ResponseEntity<Void> create(@Valid @RequestBody CategoryDTO categoryDTO) {
    Category domain = mapper.toDomain(categoryDTO);
    categoryServicePort.create(domain);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
    Category domain = mapper.toDomain(categoryDTO);
    categoryServicePort.update(id, domain);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    categoryServicePort.remove(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
