package br.com.fiap.fastfood.api.core.application.port.inbound.service;

import br.com.fiap.fastfood.api.core.application.dto.product.MenuProductDTO;
import java.util.List;

public interface MenuProductServicePort {

  List<MenuProductDTO> getAll();
  MenuProductDTO getById(Long id);
  void register(MenuProductDTO dto);
  void remove(Long id);
  void update(Long id, MenuProductDTO dto);
  List<MenuProductDTO> findAllById(List<Long> ids);

}
