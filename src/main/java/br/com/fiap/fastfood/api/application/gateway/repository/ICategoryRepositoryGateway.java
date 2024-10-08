package br.com.fiap.fastfood.api.application.gateway.repository;

import br.com.fiap.fastfood.api.application.dto.category.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepositoryGateway extends BaseRepositoryGateway<CategoryDTO, Long> {

    List<CategoryDTO> getAll();

    Optional<CategoryDTO> findByName(String name);

    void update(CategoryDTO category);

    List<Long> fetchProductsRelatedToCategory(Long id);
}
