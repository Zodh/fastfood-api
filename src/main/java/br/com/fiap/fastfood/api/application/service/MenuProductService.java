package br.com.fiap.fastfood.api.application.service;

import br.com.fiap.fastfood.api.application.dto.product.MenuProductDTO;
import java.util.List;

public interface MenuProductService {

  List<MenuProductDTO> getAll();
  MenuProductDTO getById(Long id);
  void register(MenuProductDTO dto);
  void remove(Long id);
  void update(Long id, MenuProductDTO dto);
  List<MenuProductDTO> findAllById(List<Long> ids);

}
