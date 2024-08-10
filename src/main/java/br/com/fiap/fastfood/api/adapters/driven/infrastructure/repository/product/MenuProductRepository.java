package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.product;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.MenuProductEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuProductRepository extends JpaRepository<MenuProductEntity, Long> {

  @Query(value = "SELECT DISTINCT(product_id) FROM menu_product_ingredient WHERE ingredient_id = :ingredientId", nativeQuery = true)
  List<Long> fetchProductsByIngredient(@Param("ingredientId") Long ingredientId);

}
