package br.com.fiap.fastfood.api.core.domain.model.product;

import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.Validator;
import java.util.List;
import org.springframework.util.CollectionUtils;

public class MenuProductValidator implements Validator<MenuProduct> {

  private final ProductValidator productValidator = new ProductValidator();

  @Override
  public List<ErrorDetail> validate(MenuProduct product) {
    List<ErrorDetail> errors = productValidator.validate(product);
    if (!product.isIngredient() && !product.isOptional() && CollectionUtils.isEmpty(product.getIngredients())) {
      errors.add(new ErrorDetail("product.ingredients", "Se o produto não for um ingrediente ou um opcional, ele deve conter ingredientes!"));
    }
    return List.of();
  }
}
