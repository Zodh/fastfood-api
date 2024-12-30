package br.com.fiap.fastfood.api.infrastructure.web;

import br.com.fiap.fastfood.api.adapters.controller.CategoryController;
import br.com.fiap.fastfood.api.adapters.gateway.CategoryRepositoryGatewayImpl;
import br.com.fiap.fastfood.api.adapters.gateway.MenuProductRepositoryGatewayImpl;
import br.com.fiap.fastfood.api.application.dto.category.CategoryDTO;
import br.com.fiap.fastfood.api.application.dto.category.CategoryResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryApi {

    private final CategoryController categoryController;

    @Autowired
    public CategoryApi(MenuProductRepositoryGatewayImpl menuProductRepositoryAdapter,
                       CategoryRepositoryGatewayImpl categoryRepositoryAdapter){
        this.categoryController = new CategoryController(menuProductRepositoryAdapter, categoryRepositoryAdapter);
    }

    @GetMapping
    public ResponseEntity<CategoryResponseDTO> getAll() {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryController.getAll());
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<CategoryDTO> getByIdOrName(@PathVariable String identifier) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryController.getByIdOrName(identifier));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CategoryDTO categoryDTO) {
        categoryController.create(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        categoryController.update(id, categoryDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryController.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
