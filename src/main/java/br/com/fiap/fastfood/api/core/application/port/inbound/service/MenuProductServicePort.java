package br.com.fiap.fastfood.api.core.application.port.inbound.service;

import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import java.util.List;

public interface MenuProductServicePort {

  List<MenuProduct> getAll();
  MenuProduct getById(Long id);
  void register(MenuProduct menuProduct);
  void remove(Long id);
  void update(Long id, MenuProduct menuProduct);
  List<MenuProduct> findAllById(List<Long> ids);

}
