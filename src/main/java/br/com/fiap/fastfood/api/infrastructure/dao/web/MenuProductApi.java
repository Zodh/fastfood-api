package br.com.fiap.fastfood.api.infrastructure.dao.web;

import br.com.fiap.fastfood.api.adapters.controller.MenuProductController;
import br.com.fiap.fastfood.api.adapters.gateway.MenuProductRepositoryGatewayImpl;
import br.com.fiap.fastfood.api.application.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.application.dto.product.MenuProductResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu-products")
public class MenuProductApi {

    private final MenuProductController menuProductController;

    public MenuProductApi(MenuProductRepositoryGatewayImpl menuProductRepositoryAdapter) {
        this.menuProductController = new MenuProductController(menuProductRepositoryAdapter);
    }

    @GetMapping
    public ResponseEntity<MenuProductResponseDTO> getAll() {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuProductController.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuProductDTO> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuProductController.getById(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody MenuProductDTO menuProductDTO) {
        menuProductController.create(menuProductDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody MenuProductDTO menuProductDTO) {
        menuProductController.update(id, menuProductDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        menuProductController.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
