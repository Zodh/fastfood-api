package br.com.fiap.fastfood.api.entities.category;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.entities.Validator;
import br.com.fiap.fastfood.api.entities.product.MenuProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Validator<Category> {

  private Long id;
  private String name;
  private String description;
  private boolean enabled;
  private List<MenuProduct> products;

  @Override
  public List<ErrorDetail> validate(Category category) {
    List<ErrorDetail> errors = new ArrayList<>();
    if (Objects.isNull(category)) {
      throw new DomainException(new ErrorDetail("category", "A categoria não pode ser nulo!"));
    }
    String categoryName = category.getName();
    if (StringUtils.isBlank(categoryName) || categoryName.length() < 3 || categoryName.length() > 50) {
      errors.add(new ErrorDetail("category.name", "O nome da categoria não pode ser nulo e deve conter entre 3 e 50 caracteres."));
    }
    if (StringUtils.isNotBlank(category.getDescription()) && category.getDescription().length() > 100) {
      errors.add(new ErrorDetail("category.description", "A descrição da categoria não é obrigatória, mas se preenchida deve conter até 100 caracteres!"));
    }
    if (Objects.isNull(category.getProducts()) || category.getProducts().isEmpty()) {
      errors.add(new ErrorDetail("category.products", "A categoria deve conter pelo menos um produto!"));
    }
    if ((Objects.nonNull(category.getProducts()) && !category.getProducts().isEmpty()) && category.getProducts().stream().anyMatch(mp -> Objects.isNull(mp.getPrice()) || mp.getPrice().compareTo(
            BigDecimal.ZERO) <= 0)) {
      errors.add(new ErrorDetail("category.products", "Não é permitido adicionar produtos sem preço!"));
    }
    return errors;
  }
}
