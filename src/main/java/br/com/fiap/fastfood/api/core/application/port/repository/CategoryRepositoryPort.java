package br.com.fiap.fastfood.api.core.application.port.repository;

import br.com.fiap.fastfood.api.core.application.dto.category.CategoryDTO;
import br.com.fiap.fastfood.api.core.application.port.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryPort extends BaseRepository<CategoryDTO, Long> {

    List<CategoryDTO> getAll();

    Optional<CategoryDTO> findByName(String name);

    void update(CategoryDTO category);

    List<Long> fetchProductsRelatedToCategory(Long id);
}
