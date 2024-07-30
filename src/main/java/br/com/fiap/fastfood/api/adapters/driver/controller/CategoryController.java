package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CategoryMapper;
import br.com.fiap.fastfood.api.adapters.driver.dto.category.CategoryDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.category.CategoryResponseDTO;
import br.com.fiap.fastfood.api.core.application.service.CategoryServiceImpl;
import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.CategoryServicePort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryServicePort categoryServicePort;
    private final CategoryMapper mapper;

    @Autowired
    public CategoryController(
            CategoryServiceImpl categoryServiceInboundPortImpl, CategoryMapper mapper) {
        this.categoryServicePort = categoryServiceInboundPortImpl;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<CategoryResponseDTO> getAll() {
        List<Category> allCategories = categoryServicePort.getAll();
        List<CategoryDTO> listCategoryDTO = mapper.toCategoryDTO(allCategories);
        CategoryResponseDTO response = new CategoryResponseDTO(listCategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable Long id) {
        Category category = categoryServicePort.getById(id);
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
