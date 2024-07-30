package br.com.fiap.fastfood.api.core.domain.model.category;

import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

  private Long id;
  private String name;
  private String description;
  private boolean enabled;
  private List<MenuProduct> products;

}
