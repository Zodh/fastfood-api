package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.MenuProductMapper;
import br.com.fiap.fastfood.api.adapters.driver.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.product.MenuProductResponseDTO;
import br.com.fiap.fastfood.api.core.application.service.MenuProductServicePortImpl;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.MenuProductServicePort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu-products")
public class MenuProductController {

    private final MenuProductServicePort menuProductServicePort;
    private final MenuProductMapper mapper;

    @Autowired
    public MenuProductController(
        MenuProductServicePortImpl menuProductServiceInboundPortImpl, MenuProductMapper mapper) {
        this.menuProductServicePort = menuProductServiceInboundPortImpl;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<MenuProductResponseDTO> getAll() {
        List<MenuProduct> allProducts = menuProductServicePort.getAll();
        List<MenuProductDTO> listMenuProductDTO = mapper.toMenuProductDTO(allProducts);
        MenuProductResponseDTO response = new MenuProductResponseDTO(listMenuProductDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuProductDTO> getById(@PathVariable Long id) {
        MenuProduct menuProduct = menuProductServicePort.getById(id);
        MenuProductDTO menuProductDTO = mapper.toMenuProductDTO(menuProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(menuProductDTO);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody MenuProductDTO menuProductDTO) {
        MenuProduct domain = mapper.toDomain(menuProductDTO);
        menuProductServicePort.register(domain);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody MenuProductDTO menuProductDTO) {
        MenuProduct domain = mapper.toDomain(menuProductDTO);
        menuProductServicePort.update(id, domain);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        menuProductServicePort.remove(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
