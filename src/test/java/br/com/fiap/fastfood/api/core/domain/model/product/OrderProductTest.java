package br.com.fiap.fastfood.api.core.domain.model.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderProductTest {

  @Test
  @DisplayName("Given product with three ingredients "
      + "When get price "
      + "Then calculate price based on ingredients, ingredients quantity, order product quantity and optionals")
  void getPriceCorrectly() {
    MenuProduct hamburger = mockMenuProduct(1L, BigDecimal.valueOf(0.5), "Hamburger", 1, BigDecimal.valueOf(2.50), true, true, List.of());
    MenuProduct ketchup = mockMenuProduct(2L, BigDecimal.valueOf(0.25), "Ketchup", 1, BigDecimal.valueOf(1), true, true, List.of());
    MenuProduct mustard = mockMenuProduct(3L, BigDecimal.valueOf(0.30), "Mustard", 1, BigDecimal.valueOf(1.50), true, true, List.of());
    MenuProduct hyperBurger = mockMenuProduct(4L, BigDecimal.ZERO, "Hyper Burger", 1, BigDecimal.valueOf(20.00), false, false, List.of(
        hamburger, ketchup, mustard
    ));

    OrderProduct orderHyperBurger = OrderProduct.builder().menuProduct(hyperBurger).build();
    orderHyperBurger.cloneMenuProduct();
    orderHyperBurger.calculatePrice();
    assertThat(orderHyperBurger.getPrice()).isEqualTo(hyperBurger.getPrice());
  }

  @Test
  @DisplayName("Given product with three ingredients and two optionals "
      + "When get price "
      + "Then calculate price based on ingredients, ingredients quantity, order product quantity and optionals")
  void getPriceWithOptionalCorrectly() {
    MenuProduct hamburger = mockMenuProduct(1L, BigDecimal.valueOf(0.5), "Hamburger", 1, BigDecimal.valueOf(2.50), true, true, List.of());
    MenuProduct ketchup = mockMenuProduct(2L, BigDecimal.valueOf(0.25), "Ketchup", 1, BigDecimal.valueOf(1), true, true, List.of());
    MenuProduct mustard = mockMenuProduct(3L, BigDecimal.valueOf(0.30), "Mustard", 1, BigDecimal.valueOf(1.50), true, true, List.of());
    MenuProduct hyperBurger = mockMenuProduct(4L, BigDecimal.ZERO, "Hyper Burger", 1, BigDecimal.valueOf(20.00), false, false, List.of(
        hamburger, ketchup, mustard
    ));

    OrderProduct orderHyperBurger = OrderProduct.builder().menuProduct(hyperBurger).build();
    orderHyperBurger.cloneMenuProduct();

    OrderProduct optOrderHamburger = OrderProduct.builder().menuProduct(hamburger).build();
    optOrderHamburger.cloneMenuProduct();
    optOrderHamburger.setQuantity(2);
    List<OrderProduct> optionals = List.of(optOrderHamburger);
    orderHyperBurger.setOptionals(optionals);
    orderHyperBurger.calculatePrice();

    assertThat(orderHyperBurger.getPrice()).isEqualTo(hyperBurger.getPrice().add(new BigDecimal("5.0")));
  }

  @Test
  @DisplayName("Given product with three ingredients "
      + "When get cost "
      + "Then calculate cost based on ingredients, ingredients quantity and optionals")
  void getCostCorrectly() {
    MenuProduct hamburger = mockMenuProduct(1L, BigDecimal.valueOf(0.5), "Hamburger", 1, BigDecimal.valueOf(2.50), true, true, List.of());
    MenuProduct ketchup = mockMenuProduct(2L, BigDecimal.valueOf(0.25), "Ketchup", 1, BigDecimal.valueOf(1), true, true, List.of());
    MenuProduct mustard = mockMenuProduct(3L, BigDecimal.valueOf(0.30), "Mustard", 1, BigDecimal.valueOf(1.50), true, true, List.of());

    MenuProduct hyperBurger = mockMenuProduct(4L, BigDecimal.ZERO, "Hyper Burger", 1, BigDecimal.valueOf(20.00), false, false, List.of(
        hamburger, ketchup, mustard
    ));

    OrderProduct orderHyperBurger = OrderProduct.builder().menuProduct(hyperBurger).build();
    orderHyperBurger.cloneMenuProduct();
    orderHyperBurger.calculateCost();

    assertThat(orderHyperBurger.getCost()).isEqualTo(new BigDecimal("1.05"));
  }

  @Test
  @DisplayName("Given product with three ingredients but one should be removed "
      + "When get cost "
      + "Then calculate cost based on ingredients, ingredients quantity and optionals verifying if should remove ingredient")
  void getCostWithShouldRemoveIngredientCorrectly() {
    MenuProduct hamburger = mockMenuProduct(1L, BigDecimal.valueOf(0.5), "Hamburger", 1, BigDecimal.valueOf(2.50), true, true, List.of());
    MenuProduct ketchup = mockMenuProduct(2L, BigDecimal.valueOf(0.25), "Ketchup", 1, BigDecimal.valueOf(1), true, true, List.of());
    MenuProduct mustard = mockMenuProduct(3L, BigDecimal.valueOf(0.30), "Mustard", 1, BigDecimal.valueOf(1.50), true, true, List.of());

    MenuProduct hyperBurger = mockMenuProduct(4L, BigDecimal.ZERO, "Hyper Burger", 1, BigDecimal.valueOf(20.00), false, false, List.of(
        hamburger, ketchup, mustard
    ));

    OrderProduct orderHyperBurger = OrderProduct.builder().menuProduct(hyperBurger).build();
    orderHyperBurger.cloneMenuProduct();

    OrderProduct orderProductHamburger = orderHyperBurger.getIngredients().stream().filter(i -> i.getName().equalsIgnoreCase("Hamburger")).toList().getFirst();
    orderProductHamburger.setShouldRemove(true);

    orderHyperBurger.calculateCost();

    assertThat(orderHyperBurger.getCost()).isEqualTo(new BigDecimal("0.55"));
  }


  private MenuProduct mockMenuProduct(Long id, BigDecimal cost, String name, int quantity, BigDecimal price, boolean ingredient, boolean optional, List<MenuProduct> ingredients) {
    return MenuProduct.builder()
        .id(id)
        .cost(cost)
        .name(name)
        .quantity(quantity)
        .price(price)
        .ingredient(ingredient)
        .optional(optional)
        .ingredients(ingredients)
        .build();
  }

}
