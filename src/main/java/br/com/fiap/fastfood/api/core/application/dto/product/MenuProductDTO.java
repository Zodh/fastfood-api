package br.com.fiap.fastfood.api.core.application.dto.product;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class MenuProductDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long preparationTimeInMillis;
    private int quantity;
    private BigDecimal cost;
    private List<MenuProductDTO> ingredients;
    private boolean optional;
    private boolean ingredient;

}