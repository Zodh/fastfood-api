package br.com.fiap.fastfood.api.core.domain.model.product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class MenuProduct extends Product{

    protected List<MenuProduct> optionals;
    protected List<MenuProduct> ingredients;

    @Override
    public BigDecimal getCost() {
        return isIngredient() ? this.cost : Optional.ofNullable(this.ingredients).orElse(Collections.emptyList()).stream()
            .filter(mp -> Objects.nonNull(mp) && Objects.nonNull(mp.getCost()))
            .map(mp -> mp.getCost().multiply(BigDecimal.valueOf(mp.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
