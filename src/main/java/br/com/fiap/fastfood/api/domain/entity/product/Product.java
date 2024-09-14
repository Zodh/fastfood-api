package br.com.fiap.fastfood.api.domain.entity.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class Product {

    protected Long id;
    protected String name;
    protected String description;
    protected BigDecimal price;
    protected Long preparationTimeInMillis;
    protected int quantity;
    protected BigDecimal cost;
    protected LocalDateTime created;
    protected LocalDateTime updated;
    protected boolean ingredient;
    protected boolean optional;

}
