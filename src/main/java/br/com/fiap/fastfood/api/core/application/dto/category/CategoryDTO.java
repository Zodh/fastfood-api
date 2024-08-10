package br.com.fiap.fastfood.api.adapters.driven.infrastructure.dto.category;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.dto.product.MenuProductDTO;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class CategoryDTO {

    private Long id;
    private String name;
    private String description;
    private boolean enabled;
    private List<MenuProductDTO> products;
}
