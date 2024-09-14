package br.com.fiap.fastfood.api.infrastructure.adapter.product;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.MenuProductEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MenuProductRepository extends JpaRepository<MenuProductEntity, Long> {

  @Query(value = "SELECT DISTINCT(product_id) FROM menu_product_ingredient WHERE ingredient_id = :ingredientId", nativeQuery = true)
  List<Long> fetchProductsByIngredient(@Param("ingredientId") Long ingredientId);

  Optional<MenuProductEntity> findByIdAndActiveIsTrue(Long id);

  @Modifying
  @Transactional
  @Query(value = "UPDATE MenuProductEntity mpe SET mpe.active = :active WHERE mpe.id = :id")
  void updateActive(@Param("id") Long id, @Param("active") boolean active);

}
