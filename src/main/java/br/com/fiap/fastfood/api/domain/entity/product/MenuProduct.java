package br.com.fiap.fastfood.api.domain.entity.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MenuProduct extends Product {

    protected List<MenuProduct> ingredients;
    protected boolean active;

    @Override
    public BigDecimal getCost() {
        return isIngredient() ? this.cost : Optional.ofNullable(this.ingredients).orElse(new ArrayList<>()).stream()
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

    @Override
    public void setCost(BigDecimal cost) {
        if (this.ingredient || this.optional) {
            this.cost = cost;
        } else {
            this.cost = calculateCost();
        }
    }

    private BigDecimal calculateCost() {
        List<MenuProduct> i = Optional.ofNullable(this.ingredients).orElse(new ArrayList<>());
        return i.stream()
            .filter(mp -> Objects.nonNull(mp) && Objects.nonNull(mp.getCost()))
            .map(mp -> mp.getCost().multiply(BigDecimal.valueOf(mp.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
