package br.com.fiap.fastfood.api.core.domain.service;

import br.com.fiap.fastfood.api.core.domain.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.MenuProductRepositoryPort;
import java.util.List;
import java.util.Optional;

public class MenuProductService {

  private final MenuProductRepositoryPort menuProductRepositoryPort;


  public MenuProductService(MenuProductRepositoryPort menuProductRepositoryPort) {
    this.menuProductRepositoryPort = menuProductRepositoryPort;
  }

  public List<MenuProduct> getAll() {
      return menuProductRepositoryPort.getAll();
  }

  public MenuProduct getById(Long id) {
    Optional<MenuProduct> persistedProduct = menuProductRepositoryPort.findById(id);
    return persistedProduct.orElseThrow(() -> new NotFoundException(String.format("Não foi encontrado nenhum produto com o id %d", id)));
  }

}
