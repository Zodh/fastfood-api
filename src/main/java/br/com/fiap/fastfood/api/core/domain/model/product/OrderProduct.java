package br.com.fiap.fastfood.api.core.domain.model.product;

import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class OrderProduct extends Product {

    private MenuProduct menuProduct;
    private List<OrderProduct> optionals;
    private List<OrderProduct> ingredients;
    protected boolean shouldRemove;

    @Override
    public BigDecimal getCost() {
        BigDecimal ingredientCost =
            CollectionUtils.isEmpty(this.ingredients) ? menuProduct.getCost() :
                Optional.ofNullable(this.ingredients).orElse(Collections.emptyList()).stream()
                    .filter(op -> Objects.nonNull(op) && Objects.nonNull(op.getCost())
                        && !op.isShouldRemove())
                    .map(op -> op.getCost().multiply(BigDecimal.valueOf(op.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal optionalsCost = CollectionUtils.isEmpty(optionals) ? BigDecimal.ZERO :
            optionals.stream()
                .filter(op -> Objects.nonNull(op) && Objects.nonNull(op.getCost()))
                .map(op -> op.getCost().multiply(BigDecimal.valueOf(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return ingredientCost.add(optionalsCost);
    }

    @Override
    public BigDecimal getPrice() {
        // Se não tiver adicionais no produto do pedido, deverá ser cobrado o valor original do produto (valor do menu).
        if (CollectionUtils.isEmpty(this.optionals)) {
            return menuProduct.getPrice();
        }
        BigDecimal basePrice = menuProduct.getPrice();
        BigDecimal optionalsPrice = optionals.stream()
            .filter(opt -> Objects.nonNull(opt) && Objects.nonNull(opt.getPrice()))
            .map(op -> op.getPrice().multiply(new BigDecimal(op.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return basePrice.add(optionalsPrice).multiply(BigDecimal.valueOf(quantity));
    }

    public void cloneMenuProduct() {
        this.name = menuProduct.getName();
        this.description = menuProduct.getDescription();
        this.preparationTimeInMillis = menuProduct.getPreparationTimeInMillis();
        this.ingredient = menuProduct.isIngredient();
        this.optional = menuProduct.isOptional();
        this.quantity = menuProduct.getQuantity();
        if (menuProduct.isIngredient() || menuProduct.isOptional()) {
            this.price = menuProduct.getPrice();
            this.cost = menuProduct.getCost();
        }
        if (!CollectionUtils.isEmpty(menuProduct.getIngredients())) {
            List<OrderProduct> ingredients = menuProduct.getIngredients().stream().map(i -> {
                OrderProduct op = OrderProduct.builder().menuProduct(i).build();
                op.cloneMenuProduct();
                return op;
            }).collect(Collectors.toList());
            this.setIngredients(ingredients);
        }
    }



}
