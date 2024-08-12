package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.product;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.OrderProductEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProductEntity, Long> {

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM order_product_ingredient WHERE order_ingredient_id IN (:ids)", nativeQuery = true)
  void deleteIngredientByIds(@Param("ids") List<Long> ids);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM order_product_optional WHERE order_optional_id IN (:ids)", nativeQuery = true)
  void deleteOptionalByIds(@Param("ids") List<Long> ids);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM order_product WHERE id IN (:ids)", nativeQuery = true)
  void deleteAllById(@Param("ids") List<Long> ids);

}
