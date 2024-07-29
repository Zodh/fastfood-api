package br.com.fiap.fastfood.api.core.domain.model.product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import org.springframework.util.CollectionUtils;

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

    public void setIngredients(List<MenuProduct> ingredients) {
        this.ingredients = ingredients;
        if (!CollectionUtils.isEmpty(ingredients)) {
            this.cost = calculateCost();
        }
    }

    public void setOptionals(List<MenuProduct> optionals) {
        this.optionals = optionals;
        if (!CollectionUtils.isEmpty(optionals)) {
            this.cost = calculateCost();
        }
    }

    @Override
    public void setCost(BigDecimal cost) {
        if (this.ingredient || this.optional) {
            this.cost = cost;
        } else {
            this.cost = calculateCost();
        }
    }

    private BigDecimal calculateCost() {
        List<MenuProduct> ingredients = Optional.ofNullable(this.ingredients).orElse(Collections.emptyList());
        List<MenuProduct> optionals = Optional.ofNullable(this.optionals).orElse(Collections.emptyList());
        return Stream.concat(ingredients.stream(), optionals.stream())
            .filter(mp -> Objects.nonNull(mp) && Objects.nonNull(mp.getCost()))
            .map(mp -> mp.getCost().multiply(BigDecimal.valueOf(mp.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
