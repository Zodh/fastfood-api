package br.com.fiap.fastfood.api.core.domain.model.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Product {

    protected Long id;
    protected String name;
    protected String description;
    protected BigDecimal price;
    protected Long preparationTime;
    protected int quantity;
    protected BigDecimal cost;
    private LocalDateTime created;
    private LocalDateTime updated;
    protected boolean isIngredient;
    protected boolean isOptional;
}
