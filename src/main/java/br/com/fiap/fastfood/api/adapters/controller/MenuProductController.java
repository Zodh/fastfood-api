package br.com.fiap.fastfood.api.adapters.controller;

import br.com.fiap.fastfood.api.adapters.gateway.MenuProductRepositoryGatewayImpl;
import br.com.fiap.fastfood.api.application.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.application.dto.product.MenuProductResponseDTO;
import br.com.fiap.fastfood.api.application.gateway.mapper.MenuProductMapperAppImpl;
import br.com.fiap.fastfood.api.application.service.MenuProductService;
import br.com.fiap.fastfood.api.application.service.impl.MenuProductServiceImpl;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public class MenuProductController {

  private final MenuProductService menuProductService;


  public MenuProductController(
      MenuProductRepositoryGatewayImpl menuProductRepositoryAdapter) {
    this.menuProductService = new MenuProductServiceImpl(menuProductRepositoryAdapter,
        new MenuProductMapperAppImpl());
  }

  public MenuProductResponseDTO getAll() {
    List<MenuProductDTO> allProducts = menuProductService.getAll();
    MenuProductResponseDTO response = new MenuProductResponseDTO(allProducts);
    return response;
  }


  public MenuProductDTO getById(Long id) {
    MenuProductDTO menuProductDTO = menuProductService.getById(id);
    return menuProductDTO;
  }


  public void create(MenuProductDTO menuProductDTO) {
    menuProductService.register(menuProductDTO);
  }


  public void update(Long id,
      @RequestBody MenuProductDTO menuProductDTO) {
    menuProductService.update(id, menuProductDTO);
  }


  public void delete(@PathVariable Long id) {
    menuProductService.remove(id);
  }

}
