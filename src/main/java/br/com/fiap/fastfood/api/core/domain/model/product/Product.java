package br.com.fiap.fastfood.api.core.domain.model.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;

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
