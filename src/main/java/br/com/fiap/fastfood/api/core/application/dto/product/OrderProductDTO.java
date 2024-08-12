package br.com.fiap.fastfood.api.core.application.dto.product;

import java.math.BigDecimal;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class OrderProductDTO {

    private Long id;
    private MenuProductDTO menuProduct;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal cost;
    private Long preparationTimeInMillis;
    private int quantity;
    private List<OrderProductDTO> ingredients;
    private List<OrderProductDTO> optionals;
    private boolean optional;
    private boolean ingredient;
    protected boolean shouldRemove;

}