package br.com.fiap.fastfood.api.core.domain.ports.inbound;

import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuProductServicePort {

  Page<MenuProduct> getAll(Pageable pageable);
  MenuProduct getById(Long id);
  void register(MenuProduct menuProduct);
  void remove(Long id);
  void update(Long id, MenuProduct menuProduct);
  List<MenuProduct> findAllById(List<Long> ids);

}
