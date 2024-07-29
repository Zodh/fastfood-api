package br.com.fiap.fastfood.api.core.domain.ports.inbound;

import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import java.util.List;

public interface CategoryServicePort {

  List<Category> getAll();
  Category getById(Long id);
  void create(Category category);
  void update(Long id, Category category);
  void remove(Long id);

}
