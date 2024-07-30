package br.com.fiap.fastfood.api.core.domain.repository.outbound;

import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.domain.repository.BaseRepository;

import java.util.List;

public interface CategoryRepositoryPort extends BaseRepository<Category, Long> {

    List<Category> getAll();

    void update(Category category);
}
