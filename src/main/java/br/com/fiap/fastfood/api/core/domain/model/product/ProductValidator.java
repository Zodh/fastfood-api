package br.com.fiap.fastfood.api.core.domain.model.product;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.Validator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.util.StringUtils;

public class ProductValidator implements Validator<Product> {

  @Override
  public List<ErrorDetail> validate(Product product) {
    if (Objects.isNull(product)) {
      throw new DomainException(new ErrorDetail("product", "O produto não pode ser nulo!"));
    }
    List<ErrorDetail> errors = new ArrayList<>();
    String productName = product.getName();
    if (!StringUtils.hasText(productName) || productName.length() < 3 || productName.length() > 50) {
      errors.add(new ErrorDetail("product.name", "O nome do produto não pode ser nulo e deve conter entre 3 e 50 caracteres."));
    }
    if (StringUtils.hasText(product.getDescription()) && product.getDescription().length() > 100) {
      errors.add(new ErrorDetail("product.description", "A descrição do produto não é obrigatória, mas se preenchida deve conter até 100 caracteres!"));
    }
    if (!product.isIngredient() && !product.isOptional()) {
      if (Objects.isNull(product.getPrice()) || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
        errors.add(new ErrorDetail("product.price", "O preço do produto não pode ser igual ou menor do que zero."));
      }
      if (Objects.isNull(product.getPreparationTimeInMillis()) || product.getPreparationTimeInMillis() <= 0L) {
        errors.add(new ErrorDetail("product.preparationTime", "O tempo de preparo do produto não pode ser igual ou menor do que zero."));
      }
    }
    if (!product.isOptional() && product.getQuantity() <= 0) {
        errors.add(new ErrorDetail("product.quantity", "A quantidade deve ser informada!"));
    }
    if (product.isIngredient() && (Objects.isNull(product.getCost()) || product.getCost().compareTo(BigDecimal.ZERO) <= 0)) {
      errors.add(new ErrorDetail("product.cost", "O custo do ingrediente deve ser informado!"));
    }
    return errors;
  }

}
