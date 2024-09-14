package br.com.fiap.fastfood.api.domain.entity.category;

import br.com.fiap.fastfood.api.domain.entity.product.MenuProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
