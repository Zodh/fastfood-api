package br.com.fiap.fastfood.api.core.domain.repository.outbound;

import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.domain.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryPort extends BaseRepository<Category, Long> {

    List<Category> getAll();

    Optional<Category> findByName(String name);

    void update(Category category);

    List<Long> fetchProductsRelatedToCategory(Long id);
}
