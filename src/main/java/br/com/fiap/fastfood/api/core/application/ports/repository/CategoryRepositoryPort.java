package br.com.fiap.fastfood.api.core.application.ports.repository;

import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.application.ports.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryPort extends BaseRepository<Category, Long> {

    Page<Category> getAll(Pageable pageable);

    Optional<Category> findByName(String name);

    void update(Category category);

    List<Long> fetchProductsRelatedToCategory(Long id);
}
