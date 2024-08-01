package br.com.fiap.fastfood.api.core.domain.model.product;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderProduct extends Product {

    private MenuProduct menuProduct;

    private List<OrderProduct> optionals;

    private List<OrderProduct> ingredients;

    @Override
    public BigDecimal getCost() {
        return menuProduct.getIngredients().isEmpty() ? menuProduct.cost :
                Optional.ofNullable(menuProduct.ingredients).orElse(Collections.emptyList()).stream()
                .filter(mp -> Objects.nonNull(mp) && Objects.nonNull(mp.getCost()))
                .map(mp -> mp.getCost().multiply(BigDecimal.valueOf(mp.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)

                .add(optionals.isEmpty() ? BigDecimal.ZERO :
                        optionals.stream().filter(op -> Objects.nonNull(op.menuProduct) && Objects.nonNull(op.menuProduct.getCost()))
                        .map(op -> op.menuProduct.getCost().multiply(BigDecimal.valueOf(op.menuProduct.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))

                .add(ingredients.isEmpty() ? BigDecimal.ZERO :
                        ingredients.stream().filter(op -> Objects.nonNull(op.menuProduct) && Objects.nonNull(op.menuProduct.getCost()))
                        .map(op -> op.menuProduct.getCost().multiply(BigDecimal.valueOf(op.menuProduct.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    @Override
    public BigDecimal getPrice() {
        return menuProduct.getPrice()

                .add(optionals.isEmpty() ? BigDecimal.ZERO :
                        optionals.stream().filter(op -> Objects.nonNull(op.menuProduct) && Objects.nonNull(op.menuProduct.getPrice()))
                        .map(op -> op.menuProduct.getPrice().multiply(BigDecimal.valueOf(op.menuProduct.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))

                .add(ingredients.isEmpty() ? BigDecimal.ZERO :
                        ingredients.stream().filter(op -> Objects.nonNull(op.menuProduct) && Objects.nonNull(op.menuProduct.getPrice()))
                        .map(op -> op.menuProduct.getPrice().multiply(BigDecimal.valueOf(op.menuProduct.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
