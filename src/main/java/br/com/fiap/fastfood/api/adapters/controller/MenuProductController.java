package br.com.fiap.fastfood.api.adapters.controller;

import br.com.fiap.fastfood.api.adapters.gateway.MenuProductRepositoryGatewayImpl;
import br.com.fiap.fastfood.api.application.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.application.dto.product.MenuProductResponseDTO;
import br.com.fiap.fastfood.api.application.service.IMenuProductService;
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
@RequestMapping("/menu-products")
public class MenuProductController {

  private final IMenuProductService menuProductService;

  @Autowired
  public MenuProductController(
      MenuProductRepositoryGatewayImpl menuProductRepositoryAdapter) {
    this.menuProductService = new MenuProductServiceImpl(menuProductRepositoryAdapter);
  }

  @GetMapping
  public ResponseEntity<MenuProductResponseDTO> getAll() {
    List<MenuProductDTO> allProducts = menuProductService.getAll();
    MenuProductResponseDTO response = new MenuProductResponseDTO(allProducts);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<MenuProductDTO> getById(@PathVariable Long id) {
    MenuProductDTO menuProductDTO = menuProductService.getById(id);
    return ResponseEntity.status(HttpStatus.CREATED).body(menuProductDTO);
  }

  @PostMapping
  public ResponseEntity<Void> create(@Valid @RequestBody MenuProductDTO menuProductDTO) {
    menuProductService.register(menuProductDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id,
      @RequestBody MenuProductDTO menuProductDTO) {
    menuProductService.update(id, menuProductDTO);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    menuProductService.remove(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
