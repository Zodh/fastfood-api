package br.com.fiap.fastfood.api.core.domain.model.product;

import java.math.BigDecimal;
import java.util.List;

public class ProductMock {

  public static MenuProduct mockValidMenuProduct() {
    MenuProduct menuProduct = new MenuProduct();
    menuProduct.setName("Hyper Burger");
    menuProduct.setDescription("The greater burger of our company!");
    menuProduct.setPrice(BigDecimal.valueOf(45.0));
    menuProduct.setPreparationTimeInMillis(240000L);
    menuProduct.setQuantity(1);

    MenuProduct ketchup = new MenuProduct();
    ketchup.setName("Ketchup");
    ketchup.setQuantity(1);
    ketchup.setIngredient(true);
    ketchup.setCost(new BigDecimal("0.05"));

    MenuProduct cheese = new MenuProduct();
    cheese.setName("Cheese");
    cheese.setQuantity(2);
    cheese.setIngredient(true);
    cheese.setCost(new BigDecimal("0.25"));

    MenuProduct hamburger = new MenuProduct();
    hamburger.setName("Hamburger");
    hamburger.setQuantity(1);
    hamburger.setIngredient(true);
    hamburger.setCost(new BigDecimal("2.50"));

    MenuProduct bread = new MenuProduct();
    bread.setName("Bread");
    bread.setQuantity(1);
    bread.setIngredient(true);
    bread.setCost(new BigDecimal("1.50"));

    menuProduct.setIngredients(List.of(
        ketchup,
        cheese,
        hamburger,
        bread
    ));


    return menuProduct;
  }

}
