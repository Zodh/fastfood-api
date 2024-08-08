package br.com.fiap.fastfood.api.core.application.port.inbound.service;

import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import java.util.List;

public interface CategoryServicePort {

  List<Category> getAll();
  Category getById(Long id);
  Category getByName(String name);
  void create(Category category);
  void update(Long id, Category category);
  void remove(Long id);

}
