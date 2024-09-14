package br.com.fiap.fastfood.api.application.dto.product;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class MenuProductDTO {

    protected Long id;
    protected String name;
    protected String description;
    protected BigDecimal price;
    protected Long preparationTimeInMillis;
    protected int quantity;
    protected BigDecimal cost;
    protected List<MenuProductDTO> ingredients;
    protected boolean optional;
    protected boolean ingredient;
    protected boolean active;

}