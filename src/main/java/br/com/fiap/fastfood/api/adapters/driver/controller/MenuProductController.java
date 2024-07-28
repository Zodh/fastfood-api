package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.MenuProductMapper;
import br.com.fiap.fastfood.api.adapters.driver.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.product.MenuProductResponseDTO;
import br.com.fiap.fastfood.api.core.application.service.MenuProductService;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu-products")
public class MenuProductController {

    private final MenuProductService menuProductService;
    private final MenuProductMapper mapper;

    public MenuProductController(MenuProductService menuProductService, MenuProductMapper mapper) {
        this.menuProductService = menuProductService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<MenuProductResponseDTO> getAll() {
        List<MenuProduct> allProducts = menuProductService.getAll();
        List<MenuProductDTO> listMenuProductDTO = mapper.toMenuProductDTO(allProducts);
        MenuProductResponseDTO response = new MenuProductResponseDTO(listMenuProductDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuProductDTO> getById(@PathVariable Long id) {
        MenuProduct menuProduct = menuProductService.getById(id);
        MenuProductDTO menuProductDTO = mapper.toMenuProductDTO(menuProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(menuProductDTO);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody MenuProductDTO menuProductDTO) {
        MenuProduct domain = mapper.toDomain(menuProductDTO);
        menuProductService.register(domain);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody MenuProductDTO menuProductDTO, @PathVariable Long id) {
        // TODO: Criado em: 26/07/2024 ás 08:17:44.;
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        menuProductService.remove(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
