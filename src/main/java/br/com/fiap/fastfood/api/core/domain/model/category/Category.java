package br.com.fiap.fastfood.api.core.domain.model.category;

import java.util.List;

import br.com.fiap.fastfood.api.domain.entity.product.MenuProduct;
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
