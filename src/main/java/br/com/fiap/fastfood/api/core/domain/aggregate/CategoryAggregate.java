package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.domain.model.category.CategoryValidator;
import java.util.List;
import java.util.Objects;
import lombok.Data;

@Data
public class CategoryAggregate {

  private Category categoryRoot;
  private CategoryValidator categoryValidator;

  public CategoryAggregate(Category root) {
    this.categoryRoot = root;
  }

  public CategoryAggregate(Category categoryRoot, CategoryValidator categoryValidator) {
    this.categoryRoot = categoryRoot;
    this.categoryValidator = categoryValidator;
  }

  public void create() {
    List<ErrorDetail> errors = categoryValidator.validate(categoryRoot);
    if (Objects.nonNull(errors) && !errors.isEmpty()) {
      throw new DomainException(errors);
    }
  }

  public void update(Category current) {
    categoryRoot.setId(current.getId());
    List<ErrorDetail> errors = categoryValidator.validate(categoryRoot);
    if (Objects.nonNull(errors) && !errors.isEmpty()) {
      throw new DomainException(errors);
    }
  }

  public void remove() {
    if (this.categoryRoot.isEnabled()) {
      throw new DomainException(
          new ErrorDetail("category", "Não é possível remover uma categoria ativa!"));
    }
  }

}
