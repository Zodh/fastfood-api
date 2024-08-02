package br.com.fiap.fastfood.api.core.domain.model.product;

import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.Validator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderProductValidator implements Validator<OrderProduct> {

  private final ProductValidator productValidator = new ProductValidator();

  @Override
  public List<ErrorDetail> validate(OrderProduct object) {
    List<ErrorDetail> errors = productValidator.validate(object);
    if (CollectionUtils.isEmpty(object.getIngredients())) {
      errors.add(new ErrorDetail("product.ingredients",
          "O produto do pedido deve conter ao menos um ingrediente!"));
    }
    Set<Long> orderProductIngredientIds = object.getIngredients()
        .stream()
        .filter(ingredient -> Objects.nonNull(ingredient) && Objects.nonNull(
            ingredient.getMenuProduct()))
        .map(ingredient -> ingredient.getMenuProduct().getId())
        .collect(Collectors.toSet());
    Set<Long> menuProductIngredientIds = object.getMenuProduct().getIngredients()
        .stream()
        .filter(Objects::nonNull)
        .map(MenuProduct::getId)
        .collect(Collectors.toSet());
    if (!menuProductIngredientIds.containsAll(orderProductIngredientIds)) {
      errors.add(new ErrorDetail("product.ingredients",
          "Todos os ingredientes do produto do pedido devem estar contidos no produto do menu!"));
    }
    return errors;
  }


}
