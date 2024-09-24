package br.com.fiap.fastfood.api.entities.product;

import br.com.fiap.fastfood.api.entities.exception.ErrorDetail;
import br.com.fiap.fastfood.api.entities.Validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class MenuProductValidator implements Validator<MenuProduct> {

  private final ProductValidator productValidator = new ProductValidator();

  @Override
  public List<ErrorDetail> validate(MenuProduct product) {
    List<ErrorDetail> errors = productValidator.validate(product);
    if ((Objects.isNull(product.getPrice()) || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) && !product.isOptional() && !product.isIngredient()) {
      errors.add(new ErrorDetail("product.price", "Todos os produtos do menu precisam ter um preço!"));
    }
    return errors;
  }
}
