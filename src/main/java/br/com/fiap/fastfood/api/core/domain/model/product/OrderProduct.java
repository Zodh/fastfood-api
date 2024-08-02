package br.com.fiap.fastfood.api.core.domain.model.product;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import java.util.ArrayList;
import java.util.Set;
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
    private Set<Long> ingredientsForRemoval;

    public void calculateCost() {
        BigDecimal ingredientCost =
            CollectionUtils.isEmpty(this.ingredients) ? menuProduct.getCost() :
                this.ingredients.stream()
                    .filter(op -> Objects.nonNull(op) && Objects.nonNull(op.getCost())
                        && !op.isShouldRemove())
                    .map(op -> op.getCost().multiply(BigDecimal.valueOf(op.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal optionalsCost = CollectionUtils.isEmpty(optionals) ? BigDecimal.ZERO :
            optionals.stream()
                .filter(op -> Objects.nonNull(op) && Objects.nonNull(op.getCost()))
                .map(op -> op.getCost().multiply(BigDecimal.valueOf(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.cost = ingredientCost.add(optionalsCost);
    }

    public void calculatePrice() {
        BigDecimal basePrice = menuProduct.getPrice();
        BigDecimal optionalsPrice = Optional.ofNullable(optionals).orElse(Collections.emptyList()).stream()
            .filter(opt -> Objects.nonNull(opt) && Objects.nonNull(opt.getPrice()))
            .map(op -> op.getPrice().multiply(new BigDecimal(op.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.price = basePrice.add(optionalsPrice).multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public BigDecimal getPrice() {
        if (Objects.isNull(this.price)) {
            return menuProduct.getPrice();
        }
        return this.price;
    }

    public void cloneMenuProduct() {
        this.name = menuProduct.getName();
        this.description = menuProduct.getDescription();
        this.preparationTimeInMillis = menuProduct.getPreparationTimeInMillis();
        this.ingredient = menuProduct.isIngredient();
        this.optional = menuProduct.isOptional();
        this.quantity = this.quantity > 0 ? this.quantity :menuProduct.getQuantity();
        if (menuProduct.isIngredient() || menuProduct.isOptional()) {
            this.price = menuProduct.getPrice();
            this.cost = menuProduct.getCost();
        }
        if (!CollectionUtils.isEmpty(menuProduct.getIngredients())) {
            Set<Long> shouldRemoveIngredients = Optional.ofNullable(this.ingredientsForRemoval).orElse(Collections.emptySet())
                .stream()
                .filter(i -> Objects.nonNull(i) && i > 0)
                .collect(Collectors.toSet());
            this.ingredients = menuProduct.getIngredients().stream()
                .map(i -> {
                    OrderProduct orderProductIngredient = OrderProduct.builder().menuProduct(i).build();
                    orderProductIngredient.cloneMenuProduct();
                    if (shouldRemoveIngredients.contains(i.getId())) {
                        orderProductIngredient.setShouldRemove(true);
                    }
                    return orderProductIngredient;
                })
                .collect(Collectors.toList());
        }
    }

    public void includeOptional(OrderProduct orderProduct) {
        if (Objects.isNull(this.optionals)) {
            this.optionals = new ArrayList<>();
        }
        this.optionals.add(orderProduct);
    }

    public OrderProduct findOptionalById(Long id) {
        return Optional.ofNullable(this.optionals).orElse(Collections.emptyList()).stream()
            .filter(opt -> Objects.nonNull(opt) && Objects.equals(
                opt.getId(), id)).findFirst().orElseThrow(() -> new DomainException(
                new ErrorDetail("orderProduct.optionals",
                    "Não foi encontrado um opcional com o id informado!")));
    }

    public OrderProduct findIngredientById(Long id) {
        return Optional.ofNullable(this.ingredients).orElse(Collections.emptyList()).stream()
            .filter(ingredient -> Objects.nonNull(ingredient) && Objects.equals(
                ingredient.getId(), id)).findFirst().orElseThrow(() -> new DomainException(
                new ErrorDetail("orderProduct.ingredients",
                    "Não foi encontrado um ingrediente com o id informado!")));
    }

}
