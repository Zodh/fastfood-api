package br.com.fiap.fastfood.api.core.domain.ports.inbound;

import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryServicePort {

  Page<Category> getAll(Pageable pageable);
  Category getById(Long id);
  Category getByName(String name);
  void create(Category category);
  void update(Long id, Category category);
  void remove(Long id);

}
