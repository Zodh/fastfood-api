package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.category;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.category.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query(value = "SELECT DISTINCT(product_id) FROM category_product WHERE category_id = :categoryId", nativeQuery = true)
    List<Long> fetchProductsByCategory(@Param("categoryId") Long categoryId);

    Optional<CategoryEntity> findByName(String name);
}
