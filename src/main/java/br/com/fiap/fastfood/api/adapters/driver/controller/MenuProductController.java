package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.MenuProductMapper;
import br.com.fiap.fastfood.api.adapters.driver.dto.MenuProductDTO;
import br.com.fiap.fastfood.api.core.application.service.MenuProductService;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class MenuProductController {

    private final MenuProductService menuProductService;
    private final MenuProductMapper mapper;

    public MenuProductController(MenuProductService menuProductService, MenuProductMapper mapper) {
        this.menuProductService = menuProductService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<MenuProductDTO>> getAll() {
        List<MenuProduct> allProducts = menuProductService.getAll();
        List<MenuProductDTO> listMenuProductDTO = mapper.toMenuProductDTO(allProducts);

        return ResponseEntity.status(HttpStatus.CREATED).body(listMenuProductDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuProductDTO> getById(@PathVariable Long id) {
        MenuProduct menuProduct = menuProductService.getById(id);
        MenuProductDTO menuProductDTO = mapper.toMenuProductDTO(menuProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(menuProductDTO);
    }

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody MenuProductDTO menuProductDTO) {
        MenuProduct domain = mapper.toDomain(menuProductDTO);
        menuProductService.register(domain);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody MenuProductDTO menuProductDTO, @PathVariable String id) {

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
