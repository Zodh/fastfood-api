package br.com.fiap.fastfood.api.core.application.port.inbound.service;

import br.com.fiap.fastfood.api.core.application.dto.category.CategoryDTO;
import java.util.List;

public interface CategoryServicePort {

  List<CategoryDTO> getAll();
  CategoryDTO getById(Long id);
  CategoryDTO getByName(String name);
  void create(CategoryDTO dto);
  void update(Long id, CategoryDTO dto);
  void remove(Long id);

}
