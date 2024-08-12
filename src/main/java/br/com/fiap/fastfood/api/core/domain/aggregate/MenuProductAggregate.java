package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProductValidator;
import java.util.List;
import java.util.Objects;
import lombok.Data;

@Data
public class MenuProductAggregate {

  private MenuProduct root;
  private MenuProductValidator menuProductValidator;

  public MenuProductAggregate(MenuProduct root,
      MenuProductValidator menuProductValidator) {
    this.root = root;
    this.menuProductValidator = menuProductValidator;
  }

  public void create() {
    List<ErrorDetail> errors = menuProductValidator.validate(root);
    if (Objects.nonNull(errors) && !errors.isEmpty()) {
      throw new DomainException(errors);
    }
  }

  public void update(MenuProduct current) {
    root.setId(current.getId());
    List<ErrorDetail> errors = menuProductValidator.validate(root);
    if (Objects.nonNull(errors) && !errors.isEmpty()) {
      throw new DomainException(errors);
    }
  }

}