package br.com.fiap.fastfood.api.core.domain.model.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

  @Test
  @DisplayName("Given Valid Product With Ingredients "
      + "When Get Cost "
      + "Then Calculate Ingredients Cost")
  void getCostFromIngredients() {
    MenuProduct product = ProductMock.mockValidMenuProduct();
    assertThat(product.getCost()).isEqualTo(new BigDecimal("4.55"));
  }

  @Test
  @DisplayName("Given Valid Ingredient "
      + "When Get Cost"
      + "Then Return Cost directly")
  void getIngredientCost() {
    MenuProduct bread = new MenuProduct();
    bread.setName("Bread");
    bread.setQuantity(1);
    bread.setIngredient(true);
    bread.setCost(new BigDecimal("1.50"));

    assertThat(bread.getCost()).isEqualTo(new BigDecimal("1.50"));
  }

}
